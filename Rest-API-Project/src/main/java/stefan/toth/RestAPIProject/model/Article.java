package stefan.toth.RestAPIProject.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties(value = {"author_id","category_ids"},allowSetters = true)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Created_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date created_at;

    @JsonProperty("Published_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date published_at;

    @JsonProperty("Modified_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date modified_at;

    @JsonProperty("Author")
    @ManyToOne
    private Author author;

    @Transient
    private int author_id;

    @JsonProperty("Categories")
    @OneToMany
    private List<Category> categories;

    @Transient
    private int[] category_ids;

    public Article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getPublished_at() {
        return published_at;
    }

    public void setPublished_at(Date published_at) {
        this.published_at = published_at;
    }

    public Date getModified_at() {
        return modified_at;
    }

    public void setModified_at(Date modified_at) {
        this.modified_at = modified_at;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int[] getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(int[] category_ids) {
        this.category_ids = category_ids;
    }
}
