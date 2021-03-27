package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import stefan.toth.RestAPIProject.utils.InvalidIdException;
import stefan.toth.RestAPIProject.utils.ResponseMessage;

import javax.xml.bind.ValidationException;

@ControllerAdvice
public class ExceptionHandlerController {

    private final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    ResponseMessage invalidBodyError(ValidationException e) {
        log.debug("Handling ValidationException");
        return new ResponseMessage("400", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidIdException.class)
    ResponseMessage invalidIdError(InvalidIdException e) {
        log.debug("Handling InvalidException");
        return new ResponseMessage("404", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BadCredentialsException.class)
    ResponseMessage badCredentialsError(BadCredentialsException e) {
        log.debug("Handling BadCredentialsException");
        return new ResponseMessage("400", e.getMessage());
    }
}
