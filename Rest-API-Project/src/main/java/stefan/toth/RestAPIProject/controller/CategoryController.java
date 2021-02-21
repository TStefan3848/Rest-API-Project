package stefan.toth.RestAPIProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.CategoryService;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("category")
public class CategoryController {


    @Autowired
    CategoryService categoryService;

    @GetMapping
    public Iterable<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    public Category create(@RequestBody Category category) throws ValidationException{
        if (category.getTitle() != null && category.getDescription() != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formatedTime = currentTime.format(formatter);

            category.setCreationDate(formatedTime);
            category.setLastModificationDate(formatedTime);
            return categoryService.save(category);
        }
        else throw new ValidationException("Invalid request body");

    }
}
