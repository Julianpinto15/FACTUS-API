package apiFactus.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiration;

    public boolean isTokenValid() {
        return accessToken != null && LocalDateTime.now().isBefore(tokenExpiration);
    }

    public void setTokenInfo(String accessToken, String refreshToken, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }
}