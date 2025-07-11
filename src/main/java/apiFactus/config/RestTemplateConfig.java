package apiFactus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "authRestTemplate")
    public RestTemplate authRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "apiRestTemplate")
    public RestTemplate apiRestTemplate(TokenInterceptor tokenInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(tokenInterceptor);
        return restTemplate;
    }
}