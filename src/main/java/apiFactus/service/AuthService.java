package apiFactus.service;

import apiFactus.dto.AuthRequestDTO;
import apiFactus.dto.AuthResponseDTO;
import apiFactus.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final RestTemplate authRestTemplate;
    private final TokenUtil tokenUtil;
    private final String apiUrl;
    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;

    public AuthService(
            @Qualifier("authRestTemplate") RestTemplate authRestTemplate,
            TokenUtil tokenUtil,
            @Value("${factus.api.url}") String apiUrl,
            @Value("${factus.api.client-id}") String clientId,
            @Value("${factus.api.client-secret}") String clientSecret,
            @Value("${factus.api.email}") String username,
            @Value("${factus.api.password}") String password) {
        this.authRestTemplate = authRestTemplate;
        this.tokenUtil = tokenUtil;
        this.apiUrl = apiUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
    }

    public String getAccessToken() {
        if (!tokenUtil.isTokenValid()) {
            // Si el token no es válido pero está a punto de expirar, intentamos refrescarlo
            if (tokenUtil.getRefreshToken() != null && tokenUtil.isTokenAboutToExpire()) {
                refreshToken();
            } else {
                // Si no se puede refrescar, autenticamos de nuevo
                authenticate();
            }
        }
        return tokenUtil.getAccessToken();
    }

    public void authenticate() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setGrant_type("password");
        authRequestDTO.setClient_id(clientId);
        authRequestDTO.setClient_secret(clientSecret);
        authRequestDTO.setUsername(username);
        authRequestDTO.setPassword(password);

        AuthResponseDTO authResponse = authenticate(authRequestDTO);
        tokenUtil.setTokenInfo(
                authResponse.getAccessToken(),
                authResponse.getRefreshToken(),
                authResponse.getExpiresIn()
        );
    }

    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        HttpEntity<MultiValueMap<String, String>> request = buildAuthRequest(authRequestDTO);

        logger.debug("Enviando solicitud de autenticación a: {}", apiUrl + "/oauth/token");
        logger.debug("Headers: {}", request.getHeaders());
        logger.debug("Body: {}", request.getBody());

        try {
            ResponseEntity<AuthResponseDTO> response = authRestTemplate.postForEntity(
                    apiUrl + "/oauth/token",
                    request,
                    AuthResponseDTO.class);

            logger.debug("Respuesta recibida: Status={}, Body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AuthResponseDTO authResponse = response.getBody();
                if ((authRequestDTO.getUsername() == null || authRequestDTO.getUsername().equals(username)) &&
                        (authRequestDTO.getPassword() == null || authRequestDTO.getPassword().equals(password))) {
                    tokenUtil.setTokenInfo(
                            authResponse.getAccessToken(),
                            authResponse.getRefreshToken(),
                            authResponse.getExpiresIn()
                    );
                }
                return authResponse;
            } else {
                logger.error("Fallo en la autenticación con Factus API: Status={}, Body={}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Fallo en la autenticación con Factus API: Status=" + response.getStatusCode() + ", Body=" + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Error al autenticar con Factus API: {}", e.getMessage(), e);
            throw new RuntimeException("Error al autenticar con Factus API: " + e.getMessage(), e);
        }
    }

    public void refreshToken() {
        HttpEntity<MultiValueMap<String, String>> request = buildRefreshRequest();

        logger.debug("Enviando solicitud de refresh token a: {}", apiUrl + "/oauth/token");
        logger.debug("Headers: {}", request.getHeaders());
        logger.debug("Body: {}", request.getBody());

        try {
            ResponseEntity<AuthResponseDTO> response = authRestTemplate.postForEntity(
                    apiUrl + "/oauth/token",
                    request,
                    AuthResponseDTO.class);

            logger.debug("Respuesta recibida: Status={}, Body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AuthResponseDTO authResponse = response.getBody();
                tokenUtil.setTokenInfo(
                        authResponse.getAccessToken(),
                        authResponse.getRefreshToken(),
                        authResponse.getExpiresIn()
                );
            } else {
                authenticate();
            }
        } catch (Exception e) {
            logger.error("Error al refrescar el token: {}", e.getMessage(), e);
            authenticate();
        }
    }

    private HttpEntity<MultiValueMap<String, String>> buildAuthRequest(AuthRequestDTO authRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", authRequestDTO.getGrant_type() != null ? authRequestDTO.getGrant_type() : "password");
        map.add("client_id", authRequestDTO.getClient_id() != null ? authRequestDTO.getClient_id() : clientId);
        map.add("client_secret", authRequestDTO.getClient_secret() != null ? authRequestDTO.getClient_secret() : clientSecret);
        map.add("username", authRequestDTO.getUsername() != null ? authRequestDTO.getUsername() : username);
        map.add("password", authRequestDTO.getPassword() != null ? authRequestDTO.getPassword() : password);

        return new HttpEntity<>(map, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> buildRefreshRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", tokenUtil.getRefreshToken());

        return new HttpEntity<>(map, headers);
    }
}