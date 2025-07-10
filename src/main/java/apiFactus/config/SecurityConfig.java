package apiFactus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/oauth/token", "/oauth/refresh", "/register", "/auth/google",
                                "/api/customers", "/api/products", "/api/unit-measures",
                                "/api/standard-codes", "/api/municipalities", "/api/legal-organizations",
                                "/api/tributes", "/v1/bills/validate", "/download-xml/{number}",
                                "/validate/paginated", "/show/{number}", "/download-pdf/{number}",
                                "/api/products/{id}", "/api/customers/{identification}"
                        ).permitAll()
                        .requestMatchers("/v1/bills/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos - usar setAllowedOriginPatterns para mayor flexibilidad
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "https://factusfrontend.vercel.app",
                "https://*.vercel.app",
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        // También mantener los orígenes específicos
        configuration.setAllowedOrigins(Arrays.asList(
                "https://factusfrontend.vercel.app",
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:4200"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));

        configuration.setAllowedHeaders(Collections.singletonList("*"));

        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Type"
        ));

        // Configuración crítica para el funcionamiento con credenciales
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}