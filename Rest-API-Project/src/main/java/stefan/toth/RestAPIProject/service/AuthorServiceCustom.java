package stefan.toth.RestAPIProject.service;

import stefan.toth.RestAPIProject.model.Author;

import java.util.Map;

public interface AuthorServiceCustom {
    Iterable<Author> findByCustomQuery(Map<String, String> params);
}
