package stefan.toth.RestAPIProject.service;

import org.springframework.stereotype.Service;
import stefan.toth.RestAPIProject.model.Category;

import java.util.Map;
import java.util.Optional;

@Service
public interface CategoryService {
    Iterable<Category> findAll();

    Iterable<Category> findByCustomQuery(Map<String, String> params);

    Optional<Category> findById(Integer id);

    boolean existsById(Integer id);

    Category save(Category category);

    void deleteById(Integer id);
}
