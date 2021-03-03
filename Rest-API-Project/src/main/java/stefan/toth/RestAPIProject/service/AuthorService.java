package stefan.toth.RestAPIProject.service;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Author;

public interface AuthorService extends CrudRepository<Author, Integer>, AuthorServiceCustom{
}
