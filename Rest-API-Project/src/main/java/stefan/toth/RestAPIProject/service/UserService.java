package stefan.toth.RestAPIProject.service;

import org.springframework.stereotype.Service;
import stefan.toth.RestAPIProject.model.User;

@Service
public interface UserService {
    Iterable<User> findByUsername(String username);

    Iterable<User> findAll();

    User save(User user);
}
