package backend_restaurant.mapper;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class FotoMapperHelper {

    public String mapToDto(byte[] foto) {
        return (foto != null && foto.length > 0)
                ? Base64.getEncoder().encodeToString(foto)
                : null;
    }

    public byte[] mapToEntity(String foto) {
        return (foto != null && !foto.isEmpty())
                ? Base64.getDecoder().decode(foto)
                : null;
    }
}
