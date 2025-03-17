package apiFactus.config;

import java.io.IOException;

import apiFactus.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;



@Component
public class TokenInterceptor implements ClientHttpRequestInterceptor {

    private final AuthService authService;

    // Usa inyección por constructor en lugar de @Autowired en campo
    public TokenInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().setBearerAuth(authService.getAccessToken());
        return execution.execute(request, body);
    }
}