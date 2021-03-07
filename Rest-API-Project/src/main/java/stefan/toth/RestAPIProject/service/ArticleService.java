package stefan.toth.RestAPIProject.service;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;

public interface ArticleService extends CrudRepository<Article, Integer> {
    Iterable<Article> findByAuthor(Author author);

    Iterable<Article> findByTitle(String title);
}
