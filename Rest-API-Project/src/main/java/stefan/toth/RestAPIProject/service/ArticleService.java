package stefan.toth.RestAPIProject.service;

import org.springframework.stereotype.Service;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;

import java.util.Optional;

@Service
public interface ArticleService {
    Iterable<Article> findByAuthor(Author author);

    Iterable<Article> findByTitle(String title);

    Iterable<Article> findAll();

    Optional<Article> findById(Integer id);

    boolean existsById(Integer id);

    Article save(Article article);

    void deleteById(Integer id);

    void delete(Article article);
}
