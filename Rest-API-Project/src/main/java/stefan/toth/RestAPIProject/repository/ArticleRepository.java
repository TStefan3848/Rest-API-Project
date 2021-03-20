package stefan.toth.RestAPIProject.repository;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Iterable<Article> findByTitle(String title);

    Iterable<Article> findByAuthor(Author author);
}
