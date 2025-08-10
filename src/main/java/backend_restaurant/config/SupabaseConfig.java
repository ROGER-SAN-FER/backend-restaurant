package backend_restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SupabaseConfig {
    @Value("${supabase.url}")
    private String baseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Bean
    public WebClient supabaseWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceKey)
                .defaultHeader("apikey", serviceKey)
                .build();
    }
}