package stefan.toth.RestAPIProject.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
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
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date created_at;

    @JsonProperty("Published_at")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date published_at;

    @JsonProperty("Modified_at")
    @JsonFormat(pattern="yyyy-MM-dd")
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
}
