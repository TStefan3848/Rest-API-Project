package stefan.toth.RestAPIProject.service;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Article;

public interface ArticleService extends CrudRepository<Article,Integer> {
}
