package apiFactus.service;

import apiFactus.dto.AuthRequestDTO;
import apiFactus.dto.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;


@Service
public class AuthService {

    private final RestTemplate authRestTemplate;

    @Value("${factus.api.url}")
    private String apiUrl;

    @Value("${factus.api.client-id}")
    private String clientId;

    @Value("${factus.api.client-secret}")
    private String clientSecret;

    @Value("${factus.api.email}")
    private String username;

    @Value("${factus.api.password}")
    private String password;


    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiration;

    // Usa @Qualifier para especificar cuál bean de RestTemplate quieres
    @Autowired
    public AuthService(@Qualifier("authRestTemplate") RestTemplate authRestTemplate) {
        this.authRestTemplate = authRestTemplate;
    }

    public String getAccessToken() {
        if (accessToken == null || LocalDateTime.now().isAfter(tokenExpiration)) {
            authenticate();
        }
        return accessToken;
    }

    // Método original que usa credenciales de configuración
    public void authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<AuthResponseDTO> response = authRestTemplate.postForEntity(
                apiUrl + "/oauth/token",
                request,
                AuthResponseDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            AuthResponseDTO authResponse = response.getBody();
            accessToken = authResponse.getAccessToken();
            refreshToken = authResponse.getRefreshToken();
            // Establecer expiración (típicamente 3600 segundos = 1 hora)
            tokenExpiration = LocalDateTime.now().plusSeconds(authResponse.getExpiresIn());
        } else {
            throw new RuntimeException("Fallo en la autenticación con Factus API");
        }
    }

    // Nuevo método que acepta AuthRequestDTO
    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", authRequestDTO.getGrant_type() != null ? authRequestDTO.getGrant_type() : "password");

        // Si se proporcionan credenciales en el DTO, úsalas; de lo contrario, usa los valores de configuración
        map.add("client_id", authRequestDTO.getClient_id() != null ? authRequestDTO.getClient_id() : clientId);
        map.add("client_secret", authRequestDTO.getClient_secret() != null ? authRequestDTO.getClient_secret() : clientSecret);
        map.add("username", authRequestDTO.getUsername() != null ? authRequestDTO.getUsername() : username);
        map.add("password", authRequestDTO.getPassword() != null ? authRequestDTO.getPassword() : password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<AuthResponseDTO> response = authRestTemplate.postForEntity(
                apiUrl + "/oauth/token",
                request,
                AuthResponseDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            AuthResponseDTO authResponse = response.getBody();
            // Actualiza los tokens almacenados en memoria sólo si se usaron las credenciales por defecto
            if ((authRequestDTO.getUsername() == null || authRequestDTO.getUsername().equals(username)) &&
                    (authRequestDTO.getPassword() == null || authRequestDTO.getPassword().equals(password))) {
                accessToken = authResponse.getAccessToken();
                refreshToken = authResponse.getRefreshToken();
                tokenExpiration = LocalDateTime.now().plusSeconds(authResponse.getExpiresIn());
            }
            return authResponse;
        } else {
            throw new RuntimeException("Fallo en la autenticación con Factus API");
        }
    }

    public void refreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<AuthResponseDTO> response = authRestTemplate.postForEntity(
                apiUrl + "/oauth/token",
                request,
                AuthResponseDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            AuthResponseDTO authResponse = response.getBody();
            accessToken = authResponse.getAccessToken();
            refreshToken = authResponse.getRefreshToken();
            tokenExpiration = LocalDateTime.now().plusSeconds(authResponse.getExpiresIn());
        } else {
            // Si falla el refresh, autenticamos de nuevo
            authenticate();
        }
    }
}