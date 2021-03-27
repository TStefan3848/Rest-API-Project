package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stefan.toth.RestAPIProject.model.MyUserDetails;
import stefan.toth.RestAPIProject.model.User;
import stefan.toth.RestAPIProject.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Cacheable(value = "users-load-by-username", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).iterator().next();
        return new MyUserDetails(user);
    }


    @Cacheable(value = "users-by-id", key = "#id")
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Cacheable(value = "users")
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    @Caching(evict = {@CacheEvict(value = "users-load-by-username", allEntries = true),
            @CacheEvict(value = "users", allEntries = true)},
            put = {@CachePut(value = "users-by-id", key = "#user.id"),
                    @CachePut(value = "users-by-username", key = "#user.username")
            })
    public User save(User user) {
        return userRepository.save(user);
    }

    @Cacheable(value = "users-by-username", key = "#username")
    public Iterable<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
