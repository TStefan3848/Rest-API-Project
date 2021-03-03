package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stefan.toth.RestAPIProject.model.Author;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageServiceCustom {

    @Autowired
    private AuthorService authorService;

    @Override
    @Transactional
    public void saveImageFile(Integer author_id, MultipartFile file) {
        try {
            Author author = authorService.findById(author_id).get();

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            author.setImage(byteObjects);
            authorService.save(author);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
