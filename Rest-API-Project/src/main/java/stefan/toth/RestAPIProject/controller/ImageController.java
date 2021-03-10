package stefan.toth.RestAPIProject.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.service.ImageServiceCustom;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageController {

    @Autowired
    private ImageServiceCustom imageService;

    @Autowired
    private AuthorService authorService;

    private Logger log = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/authors/{id}/image")
    public ResponseEntity<Author> postAuthorImage(@PathVariable Integer id, @RequestPart("image") MultipartFile file) throws InvalidIdException {
        log.info("Saving image " + file + "to author" + id);
        if (!authorService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Id not found in database.");
        }

        imageService.saveImageFile(id, file);

        log.info("Image saved succesfully.");
        return new ResponseEntity(authorService.findById(id).get(), HttpStatus.OK);
    }

    //Todo bad practice. not efficient byte cu byte
    @Cacheable(value = "ImageByAuthor", key = "#id")
    @GetMapping("/authors/{id}/image")
    public void renderImageFromDB(@PathVariable Integer id, HttpServletResponse response) throws InvalidIdException, IOException {
        log.info("Fetching image from database.");
        if (!authorService.existsById(id)) {
            log.info("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Id not found in database.");
        }

        if (authorService.findById(id).get().getImage() == null) {
            log.info("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Author has no image in the database");
        }

        Author author = authorService.findById(id).get();
        if (author.getImage() != null) {
            byte[] byteArray = new byte[author.getImage().length];
            int bytePosition = 0;

            for (Byte wrappedByte : author.getImage())
                byteArray[bytePosition++] = wrappedByte;

            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }
}
