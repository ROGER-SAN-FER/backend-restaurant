package backend_restaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupabaseStorageService {

    private final WebClient supabaseWebClient; // <- definido en tu @Configuration con baseUrl + headers

    @Value("${supabase.bucket}")
    private String bucket;

    @Value("${supabase.url}")              // p.ej. https://xxxx.supabase.co
    private String supabaseBaseUrl;

    /**
     * Sube un archivo (upsert=true sobrescribe si existe)
     */
    public String upload(MultipartFile file, String dir) {
        // Obtener nombre original o uno aleatorio si no existe
        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse(UUID.randomUUID() + ".bin");

        // Prefijo con timestamp para evitar sobreescribir
        String filename = System.currentTimeMillis() + "_" + originalName;

        // Construir ruta dentro del bucket
        String objectPath = (dir == null || dir.isBlank())
                ? filename
                : dir.replaceAll("^/|/$", "") + "/" + filename;

        MediaType contentType = Optional.ofNullable(file.getContentType())
                .map(MediaType::parseMediaType)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        // Subida a Supabase (upsert true por si hay otro archivo con mismo nombre exacto)
        supabaseWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/storage/v1/object/{bucket}/{object}")
                        .build(bucket, objectPath))
                .header("x-upsert", "true")
                .contentType(contentType)
                .body(BodyInserters.fromResource(file.getResource()))
                .retrieve()
                .toBodilessEntity()
                .block();

        return objectPath; // Ruta guardada en BD
    }


    /**
     * URL pública (bucket público) – no requiere llamada a la API
     */
    public String buildPublicUrl(String objectPath) {
        // OJO: el objectPath debe ir URL-encoded por segmentos
        String encoded = encodePath(objectPath);
        return supabaseBaseUrl + "/storage/v1/object/public/" + bucket + "/" + encoded;
    }

    /**
     * Signed URL (bucket privado)
     */
    public String getSignedUrl(String objectPath, int expiresInSeconds) {
        Map<String, Object> body = Map.of("expiresIn", expiresInSeconds);

        JsonNode resp = supabaseWebClient.post()
                .uri("/storage/v1/object/sign/{bucket}/{object}", bucket, objectPath)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (resp == null || resp.get("signedURL") == null) {
            throw new IllegalStateException("No se recibió 'signedURL' de Supabase");
        }
        String signed = resp.get("signedURL").asText();

        // Algunas respuestas traen solo la parte después del dominio;
        // si no empieza con "http", la completamos.
        if (!signed.startsWith("http")) {
            if (!signed.startsWith("/")) signed = "/" + signed;
            signed = supabaseBaseUrl + signed;
        }
        return signed;
    }

    /**
     * Listar objetos bajo un prefijo
     */
    public List<String> list(String prefix) {
        Map<String, Object> payload = Map.of(
                "prefix", prefix == null ? "" : prefix,
                "limit", 100,
                "offset", 0,
                "sortBy", Map.of("column", "name", "order", "asc")
        );

        JsonNode arr = supabaseWebClient.post()
                .uri("/storage/v1/object/list/{bucket}", bucket)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        List<String> names = new ArrayList<>();
        if (arr != null && arr.isArray()) {
            arr.forEach(n -> names.add(n.get("name").asText()));
        }
        return names;
    }

    /**
     * Borrar un objeto
     */
    public void delete(String objectPath) {
        if (objectPath == null || objectPath.isBlank()) {
            throw new IllegalArgumentException("objectPath no puede ser nulo o vacío");
        }
        log.info("Borrando en Supabase: bucket='{}', object='{}'", bucket, objectPath);

        supabaseWebClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/storage/v1/object/{bucket}/{object}")
                        .build(bucket, objectPath))
                .retrieve()
                .toBodilessEntity()
                .block();

        log.info("OK borrado: {}", objectPath);
    }

    private String encodePath(String path) {
        // Encode por segmentos para respetar '/'
        String[] parts = path.split("/");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = URLEncoder.encode(parts[i], StandardCharsets.UTF_8);
        }
        return String.join("/", parts);
    }
}
