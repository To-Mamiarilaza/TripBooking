package mg.tomamiarilaza.restapi.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mg.tomamiarilaza.restapi.model.User;
import mg.tomamiarilaza.restapi.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    UserRepository repo;

    @Autowired
    TokenService tokenService;

    public String login(String email, String password) {
        Optional<User> user = repo.findByEmailAndPassword(email, password);
        
        if (user.isEmpty()) {
            return null;
        }

        String token = UUID.randomUUID().toString();

        tokenService.add(token);

        return token;
    }
}
