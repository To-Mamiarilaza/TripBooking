package mg.tomamiarilaza.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.tomamiarilaza.restapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);
    
}
