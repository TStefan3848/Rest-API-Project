package stefan.toth.RestAPIProject.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceCustom {
    void saveImageFile(Integer author_id, MultipartFile file);
}
