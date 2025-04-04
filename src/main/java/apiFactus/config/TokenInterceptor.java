package apiFactus.config;

import java.io.IOException;
import java.time.LocalDateTime;

import apiFactus.service.AuthService;
import apiFactus.utils.TokenUtil;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;



@Component
public class TokenInterceptor implements ClientHttpRequestInterceptor {

    private final TokenUtil tokenUtil;

    public TokenInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        // Only add the header if we have a token available
        if (tokenUtil.isTokenValid()) {
            request.getHeaders().setBearerAuth(tokenUtil.getAccessToken());
        }

        return execution.execute(request, body);
    }
}