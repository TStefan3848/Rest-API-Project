package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import stefan.toth.RestAPIProject.utils.ErrorMessage;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;

@ControllerAdvice
public class ExceptionHandlerController {

    private Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    ErrorMessage invalidBodyError(ValidationException e) {
        log.info("Handling ValidationException");
        return new ErrorMessage("400", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidIdException.class)
    ErrorMessage invalidIdError(InvalidIdException e) {
        log.info("Handling InvalidException");
        return new ErrorMessage("404", e.getMessage());
    }
}
