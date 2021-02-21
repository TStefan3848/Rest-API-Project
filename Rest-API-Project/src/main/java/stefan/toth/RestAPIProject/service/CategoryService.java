package stefan.toth.RestAPIProject.service;

import org.springframework.data.repository.CrudRepository;
import stefan.toth.RestAPIProject.model.Category;

public interface CategoryService extends CrudRepository<Category, Integer> {
}
