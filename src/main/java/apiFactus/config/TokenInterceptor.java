package apiFactus.config;

import apiFactus.service.AuthService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenInterceptor implements ClientHttpRequestInterceptor {

    private final AuthService authService;

    public TokenInterceptor(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = authService.getToken();
        if (token != null && !token.isEmpty()) {
            request.getHeaders().set("Authorization", "Bearer " + token);
        }
        return execution.execute(request, body);
    }
}