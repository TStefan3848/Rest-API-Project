package stefan.toth.RestAPIProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;


    @GetMapping
    Iterable<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    Optional<Author> getAuthorById(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database");

        return authorService.findById(id);
    }

    @GetMapping("/search")
    Iterable<Author> getAuthorByCustomQuery(@RequestParam Map<String, String> params) throws ValidationException {
        if (params.isEmpty())
            throw new ValidationException("Invalid request. Make sure to add at least 1 search criteria.");
        return authorService.findByCustomQuery(params);
    }

    @PostMapping
    public Author create(@RequestBody Author author) throws ValidationException {
        if (author.getFirstName() == null || author.getLastName() == null)
            throw new ValidationException("Invalid request body");

        return authorService.save(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        ResponseEntity<Category> responseEntity = new ResponseEntity(authorService.findById(id), HttpStatus.OK);
        authorService.deleteById(id);
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Author> updateEntity(@RequestBody Author author) {
        if (authorService.findById(author.getId()).isPresent()) {
            if (author.getFirstName() == null)
                author.setFirstName(authorService.findById(author.getId()).get().getFirstName());
            if(author.getLastName() == null)
                author.setLastName(authorService.findById(author.getId()).get().getLastName());
            if(author.getEmail() == null)
                author.setEmail(authorService.findById(author.getId()).get().getEmail());
            return new ResponseEntity(authorService.save(author), HttpStatus.OK);
        }
        return new ResponseEntity(author, HttpStatus.BAD_REQUEST);
    }


}
