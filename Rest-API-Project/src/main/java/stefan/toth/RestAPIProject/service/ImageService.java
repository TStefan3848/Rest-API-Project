package stefan.toth.RestAPIProject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {
    void saveImageFile(Integer author_id, MultipartFile file);
}
