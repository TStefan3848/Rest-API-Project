package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.ArticleService;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.service.CategoryService;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    private Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Cacheable("Articles")
    @GetMapping
    Iterable<Article> getAllArticles() {
        log.info("Getting all articles.");
        return articleService.findAll();
    }

    @PostMapping
    public Article create(@RequestBody Article article) throws ValidationException, InvalidIdException {
        log.info("Creating new article entry.");
        if (article.getTitle() == null || article.getDescription() == null || article.getPublished_at() == null) {
            log.warn("ValidationException is getting thrown.");
            throw new ValidationException("Invalid request body");
        }

        if (article.getAuthor_id() != 0 && authorService.existsById(article.getAuthor_id()))
            article.setAuthor(authorService.findById(article.getAuthor_id()).get());
        else {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Author ID was not found in database.");
        }

        if (article.getCategory_ids() != null) {
            List<Category> categories = new ArrayList();
            for (int id_value : article.getCategory_ids()) {
                if (categoryService.existsById(id_value))
                    categories.add(categoryService.findById(id_value).get());
                else {
                    log.warn("InvalidIdException is getting thrown.");
                    throw new InvalidIdException("Category ID's were not found in database.");
                }
            }
            article.setCategories(categories);
        }

        Date date = new Date();
        article.setCreated_at(date);
        article.setModified_at(date);
        return articleService.save(article);
    }

    @Cacheable("ArticlesById")
    @GetMapping("/{id}")
    public Optional<Article> getArticleById(@PathVariable Integer id) throws InvalidIdException {
        log.info("Getting article by given ID.");
        if (!articleService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Article's Id was not found in the database.");
        }

        return articleService.findById(id);
    }

    @Cacheable("ArticlesByAuthorId")
    @GetMapping("/author/{id}")
    public Iterable<Article> getArticlesByAuthorId(@PathVariable Integer id) throws InvalidIdException {
        log.info("Getting Articles by given Author ID.");
        if (!authorService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Author Id was not found in the database.");
        }

        if (articleService.findByAuthor(authorService.findById(id).get()).iterator().hasNext()) {
            return articleService.findByAuthor(authorService.findById(id).get());
        }

        log.warn("InvalidIdException is getting thrown.");
        throw new InvalidIdException("Author has no articles in the database.");

    }

    @Caching(put = {
            @CachePut(value = "Articles"),
            @CachePut(value = "ArticlesByAuthorId", key = "#articleId"),
            @CachePut(value = "ArticlesById", key = "#articleId")
    })
    @PutMapping("/{articleId}/categories/{categoryIds}")
    public ResponseEntity<Article> setArticleCategories(@PathVariable Integer articleId, @PathVariable int[] categoryIds) throws InvalidIdException {
        log.info("Updating existing article's category field.");
        if (!articleService.existsById(articleId)) {
            log.warn("InvalidIdExceptio is getting thrown.");
            throw new InvalidIdException("Article Id was not found in the database.");
        }

        for (int i : categoryIds)
            if (!categoryService.existsById(i)) {
                log.warn("InvalidIdExceptio is getting thrown.");
                throw new InvalidIdException("Category Id was not found in database.");
            }

        List<Category> categories = new ArrayList<>();
        for (int id : categoryIds)
            categories.add(categoryService.findById(id).get());

        Article article = articleService.findById(articleId).get();
        article.setCategories(categories);
        article.setModified_at(new Date());

        log.info("Entry updated succesfully.");
        return new ResponseEntity(articleService.save(article), HttpStatus.OK);
    }

    @Caching(put = {
            @CachePut(value = "Articles"),
            @CachePut(value = "ArticlesByAuthorId", key = "#articleId"),
            @CachePut(value = "ArticlesById", key = "#articleId")
    })
    @PutMapping("/{articleId}/author/{authorId}")
    public ResponseEntity<Article> setArticleAuthor(@PathVariable Integer articleId, @PathVariable Integer authorId) throws InvalidIdException {
        log.info("Updating existing article's author field.");
        if (!articleService.existsById(articleId)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Article Id was not found in the database.");
        }
        if (!authorService.existsById(authorId)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Author Id was not found in the database.");
        }

        Article article = articleService.findById(articleId).get();
        article.setAuthor(authorService.findById(authorId).get());
        article.setModified_at(new Date());

        ResponseEntity<Article> responseEntity = new ResponseEntity(articleService.save(article), HttpStatus.OK);
        log.info("Entry updated succesfully.");
        return responseEntity;
    }

    @Caching(evict = {
            @CacheEvict(value = "Articles", key = "#id"),
            @CacheEvict(value = "ArticlesByAuthorId", key = "#id"),
            @CacheEvict(value = "ArticlesById", key = "#id")
    })
    @DeleteMapping
    public void deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        log.info("Deleting article by ID");
        if (!articleService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Article Id was not found in db");
        }

        log.info("Article deleted succesfully.");
        articleService.deleteById(id);

    }
}
