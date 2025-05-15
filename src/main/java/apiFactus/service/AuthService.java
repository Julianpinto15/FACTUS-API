package apiFactus.service;

import apiFactus.dto.AuthRequestDTO;
import apiFactus.dto.AuthResponseDTO;
import apiFactus.model.User;
import apiFactus.repository.UserRepository;
import apiFactus.config.FactusConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final FactusConfig factusConfig;

    private String accessToken;
    private String refreshToken;
    private long tokenExpirationTime;

    public AuthService(
            @Qualifier("authRestTemplate") RestTemplate restTemplate,
            ObjectMapper objectMapper,
            UserRepository userRepository,
            FactusConfig factusConfig) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.factusConfig = factusConfig;
    }

    public String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpirationTime) {
            refreshTokenWithRefreshToken();
        }
        return accessToken;
    }

    public void authenticate() {
        refreshTokenDirectly();
    }

    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        // Usar siempre las credenciales por defecto de FactusConfig
        HttpEntity<MultiValueMap<String, String>> request = buildAuthRequestWithDefaultCredentials();

        logger.debug("Enviando solicitud de autenticación a: {}", factusConfig.getUrl() + "/oauth/token");
        logger.debug("Headers: {}", request.getHeaders());
        logger.debug("Body: {}", request.getBody());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    factusConfig.getUrl() + "/oauth/token",
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
                authResponse.setTokenType(jsonNode.get("token_type").asText());
                return authResponse;
            } else {
                throw new RuntimeException("Fallo en la autenticación con Factus API: Status=" + response.getStatusCode() + ", Body=" + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Error al autenticar con Factus API: {}", e.getMessage(), e);
            throw new RuntimeException("Error al autenticar con Factus API: " + e.getMessage(), e);
        }
    }

    public AuthResponseDTO register(AuthRequestDTO authRequestDTO) {
        String email = authRequestDTO.getUsername();
        String password = authRequestDTO.getPassword() != null ? authRequestDTO.getPassword() : "default-password"; // Usar una contraseña por defecto si no se proporciona

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("El usuario con email " + email + " ya existe");
        }

        // Registrar el correo nuevo localmente
        User newUser = new User(authRequestDTO.getClient_id(), email, password);
        newUser.setActive(true); // Marcamos como activado porque usaremos las credenciales por defecto
        userRepository.save(newUser);

        // Autenticar con las credenciales por defecto de Factus
        return authenticate(new AuthRequestDTO()); // No pasamos credenciales del usuario, usamos las por defecto
    }

    public AuthResponseDTO googleSignIn(String email, String idToken) {
        logger.debug("Procesando autenticación con Google para email: {}", email);

        // Verificar o registrar el usuario localmente
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Crear un usuario con una contraseña generada (puedes usar idToken como base)
            String generatedPassword = "google-auth-" + idToken.substring(0, 8);
            user = new User(email, email, generatedPassword);
            user.setActive(true); // Marcamos como activado porque usaremos las credenciales por defecto
            userRepository.save(user);
        }

        // Autenticar con las credenciales por defecto de Factus
        return authenticate(new AuthRequestDTO());
    }

    public void refreshToken() {
        refreshTokenWithRefreshToken();
    }

    public void refreshTokenDirectly() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String encodedPassword;
        try {
            encodedPassword = URLEncoder.encode(factusConfig.getPassword(), StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error al codificar la contraseña: " + e.getMessage());
        }

        String body = String.format(
                "grant_type=password&username=%s&password=%s&client_id=%s&client_secret=%s",
                factusConfig.getEmail(),
                encodedPassword,
                factusConfig.getClientId(),
                factusConfig.getClientSecret()
        );

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Obteniendo nuevo token con credenciales desde: {}", factusConfig.getUrl() + "/oauth/token");
            ResponseEntity<String> response = restTemplate.postForEntity(
                    factusConfig.getUrl() + "/oauth/token",
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
                throw new RuntimeException("Fallo al obtener el token de acceso: Status=" + response.getStatusCode() + ", Body=" + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Error al refrescar el token: {}", e.getMessage(), e);
            throw new RuntimeException("Error al refrescar el token: " + e.getMessage(), e);
        }
    }

    public void refreshTokenWithRefreshToken() {
        if (refreshToken == null) {
            refreshTokenDirectly();
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", factusConfig.getClientId());
        body.add("client_secret", factusConfig.getClientSecret());
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            logger.debug("Refrescando token usando refresh_token desde: {}", factusConfig.getUrl() + "/oauth/token");
            ResponseEntity<String> response = restTemplate.postForEntity(
                    factusConfig.getUrl() + "/oauth/token",
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

    private HttpEntity<MultiValueMap<String, String>> buildAuthRequestWithDefaultCredentials() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", factusConfig.getClientId());
        map.add("client_secret", factusConfig.getClientSecret());
        map.add("username", factusConfig.getEmail());
        map.add("password", factusConfig.getPassword());

        return new HttpEntity<>(map, headers);
    }

    public String getToken() {
        return getAccessToken();
    }
}