package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.repository.CategoryRepository;

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
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Cacheable("categories")
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Cacheable("categories-query")
    public Iterable<Category> findByCustomQuery(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);

        Root<Category> categoryRoot = criteriaQuery.from(Category.class);
        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("Title")) {
            predicates.add(criteriaBuilder.equal(categoryRoot.get("title"), params.get("Title")));
        }
        if (params.containsKey("Description")) {
            predicates.add(criteriaBuilder.equal(categoryRoot.get("description"), params.get("Description")));
        }
        if (params.containsKey("Created_at")) {
            predicates.add(criteriaBuilder.equal(categoryRoot.get("created_at"), params.get("Created_at")));
        }
        if (params.containsKey("+Created_at")) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(categoryRoot.get("created_at"), params.get("Created_at")));
        }
        if (params.containsKey("-Created_at")) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(categoryRoot.get("created_at"), params.get("Created_at")));
        }

        if (params.containsKey("Modified_at")) {
            predicates.add(criteriaBuilder.equal(categoryRoot.get("modified_at"), params.get("Modified_at")));
        }

        if (params.containsKey("+Modified_at")) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(categoryRoot.get("modified_at"), params.get("Modified_at")));
        }

        if (params.containsKey("-Modified_at")) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(categoryRoot.get("modified_at"), params.get("Modified_at")));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Cacheable(value = "categories-id", key = "#id")
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Cacheable(value = "categories-exists-id", key = "#id")
    public boolean existsById(Integer id) {
        return categoryRepository.existsById(id);
    }

    @Caching(evict = {@CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "categories-exists-id", key = "#category.id"),
            @CacheEvict(value = "categories-query", allEntries = true)},
            put = {@CachePut(value = "categories-id", key = "#category.id")})
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Caching(evict = {
            @CacheEvict(value = "categories-id", key = "#id"),
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "categories-query", allEntries = true),
            @CacheEvict(value = "categories-exists-id", key = "#id")
    })
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

}
