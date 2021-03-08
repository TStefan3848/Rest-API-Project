package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.CategoryService;
import stefan.toth.RestAPIProject.utils.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private Logger log = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping
    public Iterable<Category> customCategorySarch(@RequestParam Map<String, String> params) {
        log.info("Getting category by custom query");
        if (params.isEmpty()) {
            log.info("There were no querries found, fetching all categories.");
            return categoryService.findAll();
        }
        return categoryService.findByCustomQuery(params);
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) throws InvalidIdException {
        log.info("Fetching category by Id.");
        if (!categoryService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Id not found in database.");
        }

        return categoryService.findById(id);

    }

    @PostMapping
    public Category create(@RequestBody Category category) throws ValidationException {
        log.info("Creating new Category entry.");
        if (category.getTitle() == null || category.getDescription() == null) {
            log.warn("ValidationException is getting thrown.");
            throw new ValidationException("Invalid request body");
        }
        Date date = new Date();
        category.setCreated_at(date);
        category.setModified_at(date);
        return categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        log.info("Deleting entry by Id");
        if (!categoryService.existsById(id)) {
            log.warn("InvalidIdException is getting thrown.");
            throw new InvalidIdException("Id not found in database.");
        }

        ResponseEntity<Category> responseEntity = new ResponseEntity(HttpStatus.OK);
        categoryService.deleteById(id);
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Category> updateEntity(@RequestBody Category category) {
        log.info("Updating existing entity.");
        if (categoryService.findById(category.getId()).isPresent()) {
            category.setCreated_at(categoryService.findById(category.getId()).get().getCreated_at());
            category.setModified_at(new Date());
            if (category.getTitle() == null)
                category.setTitle(categoryService.findById(category.getId()).get().getTitle());
            if (category.getDescription() == null)
                category.setDescription(categoryService.findById(category.getId()).get().getDescription());
            log.info("Entry updated succesfully.");
            return new ResponseEntity(categoryService.save(category), HttpStatus.OK);
        }
        log.info("Entity was not updated.");
        return new ResponseEntity(category, HttpStatus.BAD_REQUEST);
    }


}
