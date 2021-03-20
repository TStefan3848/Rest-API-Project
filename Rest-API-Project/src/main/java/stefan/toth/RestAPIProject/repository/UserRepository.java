package stefan.toth.RestAPIProject.repository;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> findByUsername(String username);
}
