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
    public ResponseEntity<Author> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        ResponseEntity<Author> responseEntity = new ResponseEntity(authorService.findById(id), HttpStatus.OK);
        Iterable<Article> articleList = articleService.findByAuthor(authorService.findById(id).get());
        for(Article article : articleList)
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

    @PostMapping("/{id}/image")
    public ResponseEntity<Author> setAuthorImage(@PathVariable Integer id, @RequestPart("imagefile") MultipartFile file) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        imageService.saveImageFile(id, file);

        return new ResponseEntity(authorService.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("{id}/image")
    public void renderImageFromDB(@PathVariable Integer id, HttpServletResponse response) throws InvalidIdException, IOException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        Author author = authorService.findById(id).get();
        if (author.getImage() != null) {
            byte[] byteArray = new byte[author.getImage().length];
            int i = 0;

            for (Byte wrappedByte : author.getImage())
                byteArray[i++] = wrappedByte;

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }


}
