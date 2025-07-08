package backend_restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // todos los endpoints
                        .allowedOrigins(
                                "http://localhost:3000",//sorin
                                "https://antojo.vercel.app",//sorin
                                "http://localhost:5173/",//roger
                                "https://frontend-pruebas.vercel.app/"//roger
                        ) //     cambia por el dominio de tu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
