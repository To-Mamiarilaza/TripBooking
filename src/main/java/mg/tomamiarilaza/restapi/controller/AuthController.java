package mg.tomamiarilaza.restapi.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mg.tomamiarilaza.restapi.dto.LoginDTO;
import mg.tomamiarilaza.restapi.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public Map login(@RequestBody LoginDTO dto) {
        String token = authService.login(dto.getEmail(), dto.getPassword());

        if (token == null) {
            return Map.of("message", "invalid login");
        }

        return Map.of("token", token);
    }
}
