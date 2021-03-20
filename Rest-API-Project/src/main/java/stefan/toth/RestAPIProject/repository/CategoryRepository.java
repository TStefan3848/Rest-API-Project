package stefan.toth.RestAPIProject.repository;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
