package stefan.toth.RestAPIProject.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.service.ImageService;
import stefan.toth.RestAPIProject.exception.InvalidIdException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthorService authorService;

    private final Logger log = LoggerFactory.getLogger(ImageController.class);

    /**
     * @param id   Identifier of the Author that will have it's Image field updated.
     * @param file Image that gets added into the database.
     * @return Response entity with HTTP Response code.
     * @throws InvalidIdException User tried to save an image to a nonexistent Author.
     */
    @PostMapping("/authors/{id}/image")
    public ResponseEntity<Author> postAuthorImage(@PathVariable Integer id, @RequestPart("image") MultipartFile file) throws InvalidIdException {
        if (!authorService.existsById(id)) {
            log.warn("Author doesn't exist in db.");
            throw new InvalidIdException("Id not found in database.");
        }

        imageService.saveImageFile(id, file);

        log.info("Saved image " + file.getName() + "to author" + id);
        return new ResponseEntity<>(authorService.findById(id).get(), HttpStatus.OK);
    }


    /**
     * @param id Identifier of the Author whose image gets fetched..
     * @param response Http response
     * @throws InvalidIdException User tried to add a photo to a nonexistent author.
     * @throws IOException        File exception
     */
    @Cacheable(value = "authors-image", key = "#id")
    @GetMapping("/authors/{id}/image")
    public void renderImageFromDB(@PathVariable Integer id, HttpServletResponse response) throws InvalidIdException, IOException {
        if (!authorService.existsById(id)) {
            throw new InvalidIdException("Id not found in database.");
        }

        if (authorService.findById(id).get().getImage() == null) {
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
