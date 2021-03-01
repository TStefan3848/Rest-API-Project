package stefan.toth.RestAPIProject.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Article;
import stefan.toth.RestAPIProject.model.Author;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.ArticleService;
import stefan.toth.RestAPIProject.service.AuthorService;
import stefan.toth.RestAPIProject.service.CategoryService;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private String dateTimeFormater(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    @GetMapping
    Iterable<Article> getAllArticles() {
        return articleService.findAll();
    }

    @PostMapping
    public Article create(@RequestBody Article article) throws ValidationException, InvalidIdException {
        if (article.getTitle() == null)
            throw new ValidationException("Invalid request body");

        if (authorService.existsById(article.getAuthor_id())) {
            Optional<Author> authorList = authorService.findById(article.getAuthor_id());
            Author author = authorList.get();
            article.setAuthor(author);
        } else throw new InvalidIdException("Author ID was not found in database.");

        List<Category> categories = new ArrayList();
        for (int id_value : article.getCategory_ids()) {
            if (categoryService.existsById(id_value)) {
                Optional<Category> categoryList = categoryService.findById(id_value);
                categories.add(categoryList.get());
            } else throw new InvalidIdException("Article ID's were not found in database.");
        }


        article.setCategories(categories);

        article.setCreated_at(dateTimeFormater(LocalDateTime.now()));
        article.setModified_at(dateTimeFormater(LocalDateTime.now()));

        return articleService.save(article);
    }
}
