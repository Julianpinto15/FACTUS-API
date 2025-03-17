package apiFactus.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "regularRestTemplate")
    public RestTemplate restTemplate() {
        // No incluyas el interceptor aquí
        return new RestTemplateBuilder()
                .build();
    }

    @Bean(name = "authRestTemplate")
    public RestTemplate authRestTemplate() {
        // RestTemplate específico para autenticación sin token
        return new RestTemplateBuilder()
                .build();
    }
}