package stefan.toth.RestAPIProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.repository.ArticleRepository;

import java.util.Optional;

@Component
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Cacheable(value = "articles-author")
    public Iterable<Article> findByAuthor(Author author) {
        return articleRepository.findByAuthor(author);
    }

    public Iterable<Article> findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Cacheable(value = "articles")
    public Iterable<Article> findAll() {
        return articleRepository.findAll();
    }

    @Cacheable(value = "articles-id", key = "#id")
    public Optional<Article> findById(Integer id) {
        return articleRepository.findById(id);
    }

    @Cacheable(value = "articles-exists-id", key = "#id")
    public boolean existsById(Integer id) {
        return articleRepository.existsById(id);
    }

    @Caching(evict = {@CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articles-author", allEntries = true),
            @CacheEvict(value = "articles-exists-id", allEntries = true),
            @CacheEvict(value = "articles-title", allEntries = true),
            @CacheEvict(value = "articles-id", allEntries = true)})
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Caching(evict = {
            @CacheEvict(value = "articles-id", key = "#id"),
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articles-title", allEntries = true),
            @CacheEvict(value = "articles-author", allEntries = true),
            @CacheEvict(value = "articles-exists-id", key = "#id")
    })
    public void deleteById(Integer id) {
        articleRepository.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "articles-id", allEntries = true),
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articles-title", allEntries = true),
            @CacheEvict(value = "articles-author", allEntries = true),
            @CacheEvict(value = "articles-exists-id", allEntries = true)
    })
    public void delete(Article article) {
        articleRepository.delete(article);
    }
}
