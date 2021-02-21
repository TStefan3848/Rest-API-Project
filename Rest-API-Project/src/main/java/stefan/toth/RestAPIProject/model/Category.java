package stefan.toth.RestAPIProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Creation-Date")
    private String creationDate;

    @JsonProperty("Last-Modification-Date")
    private String lastModificationDate;

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

    public Category() {
    }


    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(String lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Category(int id, String title, String description, String creationDate, String lastModificationDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
    }
}
