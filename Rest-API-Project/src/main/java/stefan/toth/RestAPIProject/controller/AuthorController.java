package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.service.ArticleService;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.service.ImageServiceCustom;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ImageServiceCustom imageService;

    @Autowired
    private ArticleService articleService;

    private Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Cacheable("authorsById")
    @GetMapping("/{id}")
    Optional<Author> getAuthorById(@PathVariable Integer id) throws InvalidIdException {
        log.info("Getting Author by Id");
        if (!authorService.existsById(id)) {
            log.warn("InvalidIDException is getting thrown.");
            throw new InvalidIdException("Id not found in database");
        }

        return authorService.findById(id);
    }

    @GetMapping
    Iterable<Author> getAuthorByCustomQuery(@RequestParam Map<String, String> params) {
        log.info("Getting author by custom Query");
        if (params.isEmpty()) {
            log.info("Getting all authors");
            return authorService.findAll();
        }

        return authorService.findByCustomQuery(params);
    }

    @PostMapping
    public Author create(@RequestBody Author author) throws ValidationException {
        log.info("Creating new Author entry");
        if (author.getFirstName() == null || author.getLastName() == null) {
            log.warn("ValidationException is getting thrown.");
            throw new ValidationException("Invalid request body");
        }

        return authorService.save(author);
    }

    @CacheEvict(value = {"authors", "authorsById"}, key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Author> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        log.info("Deleting author with ID" + id);
        if (!authorService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Id not found in database.");
        }


        ResponseEntity<Author> responseEntity = new ResponseEntity(HttpStatus.OK);
        Iterable<Article> articleList = articleService.findByAuthor(authorService.findById(id).get());
        for (Article article : articleList)
            articleService.delete(article);
        authorService.deleteById(id);
        return responseEntity;
    }

    @CachePut(value = "authors")
    @PutMapping
    public ResponseEntity<Author> updateEntity(@RequestBody Author author) {
        log.info("Updating existing entry.");
        if (authorService.findById(author.getId()).isPresent()) {
            if (author.getFirstName() == null)
                author.setFirstName(authorService.findById(author.getId()).get().getFirstName());
            if (author.getLastName() == null)
                author.setLastName(authorService.findById(author.getId()).get().getLastName());
            if (author.getEmail() == null)
                author.setEmail(authorService.findById(author.getId()).get().getEmail());

            log.info("Entry updated succesfully.");
            return new ResponseEntity(authorService.save(author), HttpStatus.OK);
        }
        log.info("Entry was not updated.");
        return new ResponseEntity(author, HttpStatus.BAD_REQUEST);
    }

}
