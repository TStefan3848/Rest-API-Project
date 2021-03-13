package stefan.toth.RestAPIProject.service;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.User;

public interface UserService extends CrudRepository<User, Integer> {
    Iterable<User> findByUsername(String username);
}
