package apiFactus.service;

import apiFactus.dto.AuthRequestDTO;
import apiFactus.dto.AuthResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final RestTemplate authRestTemplate;
    private final ObjectMapper objectMapper;

    private final String apiUrl;
    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;

    // Campos para manejar el token
    private String accessToken;
    private String refreshToken;
    private long tokenExpirationTime;

    public AuthService(
            @Qualifier("authRestTemplate") RestTemplate authRestTemplate,
            ObjectMapper objectMapper,
            @Value("${factus.api.url}") String apiUrl,
            @Value("${factus.api.client-id}") String clientId,
            @Value("${factus.api.client-secret}") String clientSecret,
            @Value("${factus.api.email}") String username,
            @Value("${factus.api.password}") String password) {
        this.authRestTemplate = authRestTemplate;
        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
    }

    // Método para obtener el token
    public String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpirationTime) {
            refreshTokenWithRefreshToken();
        }
        return accessToken;
    }

    // Método para compatibilidad con la interfaz original
    public void authenticate() {
        refreshTokenDirectly(); // Obtener un nuevo token directamente con las credenciales
    }

    // Método original para autenticar con un DTO
    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        HttpEntity<MultiValueMap<String, String>> request = buildAuthRequest(authRequestDTO);

        logger.debug("Enviando solicitud de autenticación a: {}", apiUrl + "/oauth/token");
        logger.debug("Headers: {}", request.getHeaders());
        logger.debug("Body: {}", request.getBody());

        try {
            ResponseEntity<String> response = authRestTemplate.postForEntity(
                    apiUrl + "/oauth/token",
                    request,
                    String.class);

            logger.debug("Respuesta recibida: Status={}, Body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                this.accessToken = jsonNode.get("access_token").asText();
                this.refreshToken = jsonNode.get("refresh_token").asText();
                long expiresIn = jsonNode.get("expires_in").asLong();
                this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);

                AuthResponseDTO authResponse = new AuthResponseDTO();
                authResponse.setAccessToken(this.accessToken);
                authResponse.setRefreshToken(this.refreshToken);
                authResponse.setExpiresIn((int) expiresIn);
                authResponse.setTokenType(jsonNode.get("token_type").asText()); // Configurar token_type

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

    // Método para refrescar el token
    public void refreshToken() {
        refreshTokenWithRefreshToken();
    }

    // Obtiene un nuevo token usando las credenciales
    public void refreshTokenDirectly() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Obteniendo nuevo token con credenciales desde: {}", apiUrl + "/oauth/token");
            ResponseEntity<String> response = authRestTemplate.postForEntity(
                    apiUrl + "/oauth/token",
                    request,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                this.accessToken = jsonNode.get("access_token").asText();
                this.refreshToken = jsonNode.get("refresh_token").asText();
                long expiresIn = jsonNode.get("expires_in").asLong();
                this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);

                logger.debug("Token refrescado con éxito");
            } else {
                logger.error("Fallo al obtener el token de acceso: Status={}, Body={}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Fallo al obtener el token de acceso: Status=" + response.getStatusCode() + ", Body=" + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Error al refrescar el token: {}", e.getMessage(), e);
            throw new RuntimeException("Error al refrescar el token: " + e.getMessage(), e);
        }
    }

    // Intenta usar el refresh token para obtener un nuevo token de acceso
    public void refreshTokenWithRefreshToken() {
        if (refreshToken == null) {
            refreshTokenDirectly(); // Si no hay refresh token, obtener uno nuevo con las credenciales
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Refrescando token usando refresh_token desde: {}", apiUrl + "/oauth/token");
            ResponseEntity<String> response = authRestTemplate.postForEntity(
                    apiUrl + "/oauth/token",
                    request,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                this.accessToken = jsonNode.get("access_token").asText();
                this.refreshToken = jsonNode.get("refresh_token").asText();
                long expiresIn = jsonNode.get("expires_in").asLong();
                this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);

                logger.debug("Token refrescado con éxito");
            } else {
                logger.warn("Fallo al refrescar token, intentando con credenciales: Status={}, Body={}", response.getStatusCode(), response.getBody());
                refreshTokenDirectly();
            }
        } catch (Exception e) {
            logger.warn("Error al refrescar con refresh token, intentando con credenciales: {}", e.getMessage());
            refreshTokenDirectly();
        }
    }

    // Método auxiliar para construir la solicitud de autenticación
    private HttpEntity<MultiValueMap<String, String>> buildAuthRequest(AuthRequestDTO authRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", authRequestDTO.getGrant_type() != null ? authRequestDTO.getGrant_type() : "password");
        map.add("client_id", authRequestDTO.getClient_id() != null ? authRequestDTO.getClient_id() : clientId);
        map.add("client_secret", authRequestDTO.getClient_secret() != null ? authRequestDTO.getClient_secret() : clientSecret);

        if ("password".equals(authRequestDTO.getGrant_type())) {
            map.add("username", authRequestDTO.getUsername() != null ? authRequestDTO.getUsername() : username);
            map.add("password", authRequestDTO.getPassword() != null ? authRequestDTO.getPassword() : password);
        } else if ("refresh_token".equals(authRequestDTO.getGrant_type())) {
            map.add("refresh_token", authRequestDTO.getRefresh_token());
        }

        return new HttpEntity<>(map, headers);
    }

    // Método para compatibilidad con el primer documento
    public String getToken() {
        return getAccessToken();
    }
}