package apiFactus.controller;

import apiFactus.dto.AuthRequestDTO;
import apiFactus.dto.AuthResponseDTO;
import apiFactus.dto.GoogleSignInRequest;
import apiFactus.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Value("${factus.api.client-id}")
    private String clientId;

    @Value("${factus.api.client-secret}")
    private String clientSecret;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<?> getToken(
            @RequestParam(defaultValue = "password") String grant_type,
            @RequestParam String client_id,
            @RequestParam String client_secret,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String refresh_token) {
        logger.debug("Recibida solicitud de autenticación: grant_type={}, client_id={}, username={}", grant_type, client_id, username);
        try {
            if (!client_id.equals(clientId) || !client_secret.equals(clientSecret)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("client_id o client_secret inválidos");
            }

            AuthRequestDTO authRequestDTO = new AuthRequestDTO(grant_type, client_id, client_secret, username, password, refresh_token);
            AuthResponseDTO response = authService.authenticate(authRequestDTO);
            logger.debug("Autenticación exitosa: access_token={}", response.getAccessToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error durante la autenticación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticación fallida: " + e.getMessage());
        }
    }

    @PostMapping("/oauth/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestParam String client_id,
            @RequestParam String client_secret,
            @RequestParam String refresh_token) {
        logger.debug("Recibida solicitud de refresco de token: client_id={}", client_id);
        try {
            if (!client_id.equals(clientId) || !client_secret.equals(clientSecret)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("client_id o client_secret inválidos");
            }

            AuthRequestDTO authRequestDTO = new AuthRequestDTO();
            authRequestDTO.setGrant_type("refresh_token");
            authRequestDTO.setClient_id(client_id);
            authRequestDTO.setClient_secret(client_secret);
            authRequestDTO.setRefresh_token(refresh_token);

            AuthResponseDTO response = authService.authenticate(authRequestDTO);
            logger.debug("Token refrescado con éxito: access_token={}", response.getAccessToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al refrescar el token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token refresh failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) {
        logger.debug("Recibida solicitud de registro: username={}, email={}", username, email);
        try {
            AuthRequestDTO authRequestDTO = new AuthRequestDTO("password", clientId, clientSecret, email, password, null);
            AuthResponseDTO response = authService.register(authRequestDTO);
            logger.debug("Registro exitoso: access_token={}", response.getAccessToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error durante el registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registro exitoso localmente, pero usa las credenciales por defecto de Factus para autenticación.");
        }
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> googleSignIn(@RequestBody GoogleSignInRequest request) {
        logger.debug("Recibida solicitud de autenticación con Google: email={}", request.getEmail());
        try {
            AuthResponseDTO response = authService.googleSignIn(request.getEmail(), request.getIdToken());
            logger.debug("Autenticación con Google exitosa: access_token={}", response.getAccessToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error durante la autenticación con Google: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticación con Google fallida: " + e.getMessage());
        }
    }

}

