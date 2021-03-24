package stefan.toth.RestAPIProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RestApiProjectApplication {

    //TODO - Ignore log files + delete already pushed logs.
    public static void main(String[] args) {
            SpringApplication.run(RestApiProjectApplication.class, args);
    }

}
