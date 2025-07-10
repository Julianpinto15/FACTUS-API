package apiFactus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${CORS_ALLOWED_ORIGINS:https://factusfrontend.vercel.app,http://localhost:3000,http://localhost:4200}")
    private String allowedOrigins;

    @Value("${RAILWAY_PUBLIC_DOMAIN:#{null}}")
    private String railwayDomain;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/health", "/api/status", "/debug/**",
                                "/oauth/token", "/oauth/refresh", "/register", "/auth/google",
                                "/api/customers", "/api/products", "/api/unit-measures",
                                "/api/standard-codes", "/api/municipalities", "/api/legal-organizations",
                                "/api/tributes", "/v1/bills/validate", "/download-xml/{number}",
                                "/validate/paginated", "/show/{number}", "/download-pdf/{number}",
                                "/api/products/{id}", "/api/customers/{identification}",
                                "/actuator/**"
                        ).permitAll()
                        .requestMatchers("/v1/bills/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Crear una lista modificable
        List<String> allowedPatterns = new ArrayList<>(Arrays.asList(
                "https://factusfrontend.vercel.app",
                "https://*.vercel.app",
                "https://*.railway.app",
                "https://*.up.railway.app",
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        // Agregar dominio de Railway si existe
        if (railwayDomain != null && !railwayDomain.isEmpty()) {
            allowedPatterns.add("https://" + railwayDomain);
        }

        // Log para debugging
        System.out.println("CORS: Patrones de orígenes permitidos: " + allowedPatterns);

        configuration.setAllowedOriginPatterns(allowedPatterns);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Type",
                "X-Requested-With"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}