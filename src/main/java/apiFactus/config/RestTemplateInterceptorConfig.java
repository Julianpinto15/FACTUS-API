package apiFactus.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateInterceptorConfig {

    @Bean
    public RestTemplate restTemplateWithInterceptor(
            @Qualifier("regularRestTemplate") RestTemplate restTemplate,
            TokenInterceptor tokenInterceptor) {

        restTemplate.getInterceptors().add(tokenInterceptor);
        return restTemplate;
    }
}
