package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.repository.AuthorRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EntityManager entityManager;

    private void simulateSlowService() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Cacheable("authors-query")
    public Iterable<Author> findByCustomQuery(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);

        Root<Author> categoryRoot = criteriaQuery.from(Author.class);
        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("firstName")) {
            predicates.add(criteriaBuilder.equal(categoryRoot.get("firstName"), params.get("firstName")));
        }
        if (params.containsKey("lastName")) {
            predicates.add(criteriaBuilder.equal(categoryRoot.get("lastName"), params.get("lastName")));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Cacheable(value = "authors-id", key = "#id")
    public Optional<Author> findById(Integer id) {
        simulateSlowService();
        return authorRepository.findById(id);
    }

    @Cacheable(value = "authors-exists-id", key = "#id")
    public boolean existsById(Integer id) {
        return authorRepository.existsById(id);
    }

    @Caching(evict = {@CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "authors-query", allEntries = true),
            @CacheEvict(value = "authors-exists-id", allEntries = true),
            @CacheEvict(value = "authors-id", allEntries = true)}
    )
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Caching(evict = {
            @CacheEvict(value = "authors-id", key = "#id"),
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "authors-query", allEntries = true),
            @CacheEvict(value = "authors-exists-id", key = "#id")
    })
    public void deleteById(Integer id) {
        authorRepository.deleteById(id);
    }

    @Cacheable("authors")
    public Iterable<Author> findAll() {
        simulateSlowService();
        return authorRepository.findAll();
    }
}
