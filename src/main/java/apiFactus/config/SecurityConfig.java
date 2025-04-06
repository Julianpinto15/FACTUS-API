package apiFactus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth/token", "/oauth/refresh").permitAll()
                        .requestMatchers("/invoices").authenticated()
                        .anyRequest().authenticated() // Proteger otros endpoints
                )
                .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF

        return http.build();
    }
}