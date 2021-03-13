package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.User;
import stefan.toth.RestAPIProject.service.UserService;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public void signUp(@RequestBody User user) throws ValidationException {
        log.info("Signing up a new user");
        if (user.getUsername() == null || user.getPassword() == null) {
            log.warn("ValidationException is getting thrown");
            throw new ValidationException("Invalid request body");
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            log.warn("ValidationException is getting thrown.");
            throw new ValidationException("Username already exists in the database.");
        }

        log.info("User signed up succesfully");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.save(user);
    }

    @Cacheable("Users")
    @GetMapping
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

}
