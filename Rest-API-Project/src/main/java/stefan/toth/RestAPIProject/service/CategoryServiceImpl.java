package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import stefan.toth.RestAPIProject.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryServiceImpl implements CategoryServiceCustom {

    @Autowired
    private EntityManager entityManager;

    @Cacheable("Categories")
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

}
