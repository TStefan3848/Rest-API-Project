package stefan.toth.RestAPIProject.repository;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
}
