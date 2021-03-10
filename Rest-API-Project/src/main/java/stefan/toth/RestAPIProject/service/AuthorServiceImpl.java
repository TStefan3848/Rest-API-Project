package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import stefan.toth.RestAPIProject.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthorServiceImpl implements AuthorServiceCustom {

    @Autowired
    private EntityManager entityManager;

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
}
