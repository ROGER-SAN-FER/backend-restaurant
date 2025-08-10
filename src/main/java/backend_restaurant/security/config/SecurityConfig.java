package backend_restaurant.security.config;

import backend_restaurant.security.service.UserDetailsServiceImpl;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        {
                            auth
                                    // PERMITIR OPTIONS
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    // Permitir todos los GET sin autenticación
                                    .requestMatchers(HttpMethod.GET, "/api/platillos/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/tipos/**").permitAll()
                                    // Restringir POST, PUT, DELETE solo a ADMIN
                                    .requestMatchers(HttpMethod.POST, "/api/platillos/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.POST, "/api/tipos/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PUT, "/api/platillos/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PUT, "/api/tipos/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.DELETE, "/api/platillos/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.DELETE, "/api/tipos/**").hasRole("ADMIN")

                                    //Para swagger (documentacion)
                                    .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                                    //Para actuator (metrics, info, health, etc)
                                    .requestMatchers(EndpointRequest.to("health","info")).permitAll()
                                    .requestMatchers(EndpointRequest.to("metrics","caches")).hasAnyRole("ADMIN","ACTUATOR")
                                    .requestMatchers("/actuator/metrics/**", "/actuator/caches/**").hasAnyRole("ADMIN","ACTUATOR")
                                    .anyRequest().denyAll();
                        }
                ).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

}
