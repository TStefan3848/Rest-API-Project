package stefan.toth.RestAPIProject.service;

import org.springframework.stereotype.Service;
import stefan.toth.RestAPIProject.model.Author;

import java.util.Map;
import java.util.Optional;

@Service
public interface AuthorService {
    Iterable<Author> findAll();

    Iterable<Author> findByCustomQuery(Map<String, String> params);

    Optional<Author> findById(Integer id);

    boolean existsById(Integer id);

    Author save(Author author);

    void deleteById(Integer id);
}
