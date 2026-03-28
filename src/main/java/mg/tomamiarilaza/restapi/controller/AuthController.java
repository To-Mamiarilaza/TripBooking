package mg.tomamiarilaza.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mg.tomamiarilaza.restapi.dto.LoginDTO;
import mg.tomamiarilaza.restapi.dto.LoginResponse;
import mg.tomamiarilaza.restapi.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        LoginResponse response = authService.login(dto.getEmail(), dto.getPassword());

        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "Email ou mot de passe invalide"));
        }

        return ResponseEntity.ok(response);
    }
}
