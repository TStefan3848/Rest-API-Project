package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.User;
import stefan.toth.RestAPIProject.service.UserService;
import stefan.toth.RestAPIProject.model.UserRole;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public void signUp(@RequestBody User user) throws ValidationException {
        if (user.getUsername() == null || user.getPassword() == null) {
            log.warn("ValidationException was thrown.");
            throw new ValidationException("Invalid request body");
        }

        if (userService.findByUsername(user.getUsername()).iterator().hasNext()) {
            log.warn("ValidationException was thrown.");
            throw new ValidationException("Username already exists in the database.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole("USER"));
        user.setRoles(userRoles);
        userService.save(user);

        log.info("User was signed up");
    }

    @GetMapping
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
