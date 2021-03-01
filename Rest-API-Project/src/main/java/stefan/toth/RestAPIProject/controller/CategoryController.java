package stefan.toth.RestAPIProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.CategoryService;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public Iterable<Category> getAllCategories() {
        return categoryService.findAll();
    }

    private String dateTimeFormater(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    @GetMapping("/search")
    public Iterable<Category> customCategorySarch(@RequestParam Map<String, String> params) throws ValidationException {
        if (params.isEmpty())
            throw new ValidationException("Invalid request. Make sure to add at least 1 search criteria.");

        return categoryService.findByCustomQuery(params);
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) throws InvalidIdException {
        if (!categoryService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        return categoryService.findById(id);

    }

    @PostMapping
    public Category create(@RequestBody Category category) throws ValidationException {
        if (category.getTitle() == null || category.getDescription() == null)
            throw new ValidationException("Invalid request body");

        category.setCreated_at(dateTimeFormater(LocalDateTime.now()));
        category.setModified_at(dateTimeFormater(LocalDateTime.now()));
        return categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        if (!categoryService.existsById(id))
            throw new InvalidIdException("Id not found in database.");

        ResponseEntity<Category> responseEntity = new ResponseEntity(categoryService.findById(id), HttpStatus.OK);
        categoryService.deleteById(id);
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Category> updateEntity(@RequestBody Category category) {
        if (categoryService.findById(category.getId()).isPresent()) {
            category.setCreated_at(categoryService.findById(category.getId()).get().getCreated_at());
            category.setModified_at(dateTimeFormater(LocalDateTime.now()));
            if (category.getTitle() == null)
                category.setTitle(categoryService.findById(category.getId()).get().getTitle());
            if(category.getDescription() == null)
                category.setDescription(categoryService.findById(category.getId()).get().getDescription());
            return new ResponseEntity(categoryService.save(category), HttpStatus.OK);
        }
        return new ResponseEntity(category, HttpStatus.BAD_REQUEST);
    }


}
