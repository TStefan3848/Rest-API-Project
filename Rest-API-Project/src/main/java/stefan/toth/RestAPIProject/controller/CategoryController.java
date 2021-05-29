package stefan.toth.RestAPIProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stefan.toth.RestAPIProject.model.Category;
import stefan.toth.RestAPIProject.service.CategoryService;
import stefan.toth.RestAPIProject.exception.InvalidIdException;

import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    /**
     * @param params Map that contains query parameters used for searching.
     * @return All categories that respect the given queries. If none were given returns all categories.
     */
    @GetMapping
    public Iterable<Category> customCategorySearch(@RequestParam Map<String, String> params) {
        if (params.isEmpty()) {
            log.info("Fetched all categories.");
            return categoryService.findAll();
        }

        log.info("Fetched categories by given query parameters.");
        return categoryService.findByCustomQuery(params);
    }

    /**
     * @param id Identifier of the Category that we want to fetch
     * @return Category that has the given ID.
     * @throws InvalidIdException When the given ID doesn't exist in the db.
     */
    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) throws InvalidIdException {
        if (!categoryService.existsById(id)) {
            log.warn("User inserted an invalid ID.");
            throw new InvalidIdException("Id not found in database.");
        }

        log.info("Fetched category by ID: " + id);
        return categoryService.findById(id);

    }

    /**
     * @param category A category object that gets added into the database.
     * @return The category object that was added and the HTTP Response code.
     * @throws ValidationException User inserted a wrong request body.
     */
    @PostMapping
    public Category create(@RequestBody Category category) throws ValidationException {
        if (category.getTitle() == null || category.getDescription() == null) {
            log.warn("User inserted an invalid request body.");
            throw new ValidationException("Invalid request body");
        }

        log.info("New Category was added into the database.");
        Date date = new Date();
        category.setCreated_at(date);
        category.setModified_at(date);
        return categoryService.save(category);
    }

    /**
     * @param id Identifier of the Category we are looking to delete.
     * @return Response entity that contains the HTTP Response code.
     * @throws InvalidIdException User is  trying to delete a category that doesn't exist in the db.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Integer id) throws InvalidIdException {
        if (!categoryService.existsById(id)) {
            log.warn("User is trying to delete nonexistent category.");
            throw new InvalidIdException("Id not found in database.");
        }

        ResponseEntity<Category> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        log.info("Category with ID: " + id + " was deleted.");
        categoryService.deleteById(id);
        return responseEntity;
    }

    /**
     * @param category Category object that will be updated, found by category.id
     * @return Response entity that contains the HTTP Response code.
     */
    @PutMapping
    public ResponseEntity<Category> updateEntity(@RequestBody Category category) {

        if (categoryService.findById(category.getId()).isPresent()) {
            category.setCreated_at(categoryService.findById(category.getId()).get().getCreated_at());
            category.setModified_at(new Date());
            if (category.getTitle() == null)
                category.setTitle(categoryService.findById(category.getId()).get().getTitle());
            if (category.getDescription() == null)
                category.setDescription(categoryService.findById(category.getId()).get().getDescription());
            log.info("Existing entity was updated.");
            return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
        }
        log.warn("Invalid category to update.");
        return new ResponseEntity<>(category, HttpStatus.BAD_REQUEST);
    }


}
