package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.service.ArticleService;
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

    @Autowired
    private ArticleService articleService;

    private final Logger log = LoggerFactory.getLogger(ArticleController.class);

    /**
     * @param params Simple Map that contains all given queries
     * @return Returns all authors from the database by given queries.
     */
    @GetMapping
    public Iterable<Author> getAuthorByCustomQuery(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            log.info("Fetched all authors.");
            return authorService.findAll();
        }

        log.info("Fetched authors by query.");
        return authorService.findByCustomQuery(params);
    }

    /**
     * @param id - Author's ID that we are looking for.
     * @return - returns author with given id
     * @throws InvalidIdException If there is no author with given ID.
     */
    @GetMapping("/{id}")
    public Optional<Author> getAuthorById(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id)) {
            log.warn("User tried to fetch an invalid author.");
            throw new InvalidIdException("Id not found in database");
        }

        log.info("Fetched author with ID: " + id);
        return authorService.findById(id);
    }

    /**
     * @param author Author object that we will add to the database.
     * @return - HTTP Response code
     * @throws ValidationException If the request body is invalid.
     */
    @PostMapping
    public Author create(@RequestBody Author author) throws ValidationException {
        if (author.getFirstName() == null || author.getLastName() == null) {
            log.warn("User inserted an invalid request body.");
            throw new ValidationException("Invalid request body");
        }

        log.info("Added author to db.");
        return authorService.save(author);
    }

    /**
     * @param id Identifier of the Author that gets deleted
     * @return Response entity containing deleted author and HTTP Response code
     * @throws InvalidIdException If there is no author with given ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id)) {
            log.warn("User tried to find an nonexistent author.");
            throw new InvalidIdException("Id not found in database.");
        }


        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        Iterable<Article> articleList = articleService.findByAuthor(authorService.findById(id).get());
        for (Article article : articleList) {
            log.info("Deleted article who's Author is " + authorService.findById(id).get().getLastName());
            articleService.delete(article);
        }

        authorService.deleteById(id);
        log.info("Deleted author with ID" + id);
        return responseEntity;
    }

    /**
     * @param author An Author object that is used to update an existing Author
     *               found by author.id
     * @return Response entity containing updated author and HTTP Response code
     */
    @PutMapping
    public ResponseEntity<Author> updateEntity(@RequestBody Author author) {
        if (authorService.findById(author.getId()).isPresent()) {
            if (author.getFirstName() == null)
                author.setFirstName(authorService.findById(author.getId()).get().getFirstName());
            if (author.getLastName() == null)
                author.setLastName(authorService.findById(author.getId()).get().getLastName());
            if (author.getEmail() == null)
                author.setEmail(authorService.findById(author.getId()).get().getEmail());

            log.info("Author updated successfully.");
            return new ResponseEntity<>(authorService.save(author), HttpStatus.OK);
        }
        log.warn("Author doesn't exist in db.");
        return new ResponseEntity<>(author, HttpStatus.BAD_REQUEST);
    }

}
