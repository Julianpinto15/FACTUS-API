package apiFactus.controller;

import apiFactus.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {

    private final AuthService authService;

    public RefreshTokenController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken() {
        try {
            authService.refreshToken();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token refresh failed: " + e.getMessage());
        }
    }
}