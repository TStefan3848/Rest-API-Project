package stefan.toth.RestAPIProject.service;

import stefan.toth.RestAPIProject.model.Category;

import java.util.Map;

public interface CategoryServiceCustom {
    Iterable<Category> findByCustomQuery(Map<String, String> params);
}
