package apiFactus.controller;

import apiFactus.dto.AuthRequestDTO;
import apiFactus.dto.AuthResponseDTO;
import apiFactus.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

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
            @RequestParam(required = false) String refresh_token) { // Hacer refresh_token opcional
        logger.debug("Recibida solicitud de autenticación: grant_type={}, client_id={}, username={}", grant_type, client_id, username);
        try {
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
        try {
            AuthRequestDTO authRequestDTO = new AuthRequestDTO();
            authRequestDTO.setGrant_type("refresh_token");
            authRequestDTO.setClient_id(client_id);
            authRequestDTO.setClient_secret(client_secret);
            authRequestDTO.setRefresh_token(refresh_token); // Pasar el refresh_token

            AuthResponseDTO response = authService.authenticate(authRequestDTO); // Usar authenticate con el DTO
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token refresh failed: " + e.getMessage());
        }
    }


}