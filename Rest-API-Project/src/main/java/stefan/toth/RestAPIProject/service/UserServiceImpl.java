package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import stefan.toth.RestAPIProject.model.User;
import stefan.toth.RestAPIProject.repository.UserRepository;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private void simulateSlowService() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Cacheable(value = "users-username", key = "#username")
    public Iterable<User> findByUsername(String username) {
        simulateSlowService();
        return userRepository.findByUsername(username);
    }

    @Cacheable("users")
    public Iterable<User> findAll() {
        simulateSlowService();
        return userRepository.findAll();
    }

    @Caching(evict = {@CacheEvict("users"),
            @CacheEvict(value = "users-username", key = "#user.username")})
    public User save(User user) {
        simulateSlowService();
        return userRepository.save(user);
    }
}
