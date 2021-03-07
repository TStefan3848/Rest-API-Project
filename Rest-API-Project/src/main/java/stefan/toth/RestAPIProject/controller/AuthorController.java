package stefan.toth.RestAPIProject.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.service.ArticleService;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.service.ImageServiceCustom;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping("/{id}")
    Optional<Author> getAuthorById(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database");

        return authorService.findById(id);
    }

    @GetMapping
    Iterable<Author> getAuthorByCustomQuery(@RequestParam Map<String, String> params) {
        if (params.isEmpty())
            return authorService.findAll();
        return authorService.findByCustomQuery(params);
    }

    @PostMapping
    public Author create(@RequestBody Author author) throws ValidationException {
        if (author.getFirstName() == null || author.getLastName() == null)
            throw new ValidationException("Invalid request body");

        return authorService.save(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Author> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        ResponseEntity<Author> responseEntity = new ResponseEntity(HttpStatus.OK);
        Iterable<Article> articleList = articleService.findByAuthor(authorService.findById(id).get());
        for (Article article : articleList)
            articleService.delete(article);
        authorService.deleteById(id);
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Author> updateEntity(@RequestBody Author author) {
        if (authorService.findById(author.getId()).isPresent()) {
            if (author.getFirstName() == null)
                author.setFirstName(authorService.findById(author.getId()).get().getFirstName());
            if (author.getLastName() == null)
                author.setLastName(authorService.findById(author.getId()).get().getLastName());
            if (author.getEmail() == null)
                author.setEmail(authorService.findById(author.getId()).get().getEmail());
            return new ResponseEntity(authorService.save(author), HttpStatus.OK);
        }
        return new ResponseEntity(author, HttpStatus.BAD_REQUEST);
    }

}
