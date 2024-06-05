package truyenconvert.server.modules.users.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.modules.users.model.User;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String email);
    User save(User user);
}
