package apiFactus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://factusfrontend.vercel.app"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth/token", "/oauth/refresh",
                                "/register",
                                "/auth/google",
                                "/api/customers",
                                "/api/products",
                                "/api/unit-measures",
                                "/api/standard-codes",
                                "/api/municipalities",
                                "/api/legal-organizations",
                                "/api/tributes",
                                "/v1/bills/validate",
                                "/download-xml/{number}",
                                "/validate/paginated",
                                "/show/{number}",
                                "/download-pdf/{number}",
                                "/api/products/{id}",
                                "/api/customers/{identification}").permitAll()
                        .requestMatchers("/v1/bills/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}