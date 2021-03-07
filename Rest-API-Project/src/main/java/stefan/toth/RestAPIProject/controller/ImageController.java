package stefan.toth.RestAPIProject.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/authors/{id}/image")
    public ResponseEntity<Author> postAuthorImage(@PathVariable Integer id, @RequestPart("image") MultipartFile file) throws InvalidIdException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        imageService.saveImageFile(id, file);

        return new ResponseEntity(authorService.findById(id).get(), HttpStatus.OK);
    }

    //Todo bad practice. not efficient byte cu byte
    @GetMapping("/authors/{id}/image")
    public void renderImageFromDB(@PathVariable Integer id, HttpServletResponse response) throws InvalidIdException, IOException {
        if (!authorService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        if (authorService.findById(id).get().getImage() == null)
            throw new InvalidIdException("Author has no image in the database");

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
