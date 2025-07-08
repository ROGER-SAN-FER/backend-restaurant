package backend_restaurant.security.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String role; // Ej: "USER" o "ADMIN"
}