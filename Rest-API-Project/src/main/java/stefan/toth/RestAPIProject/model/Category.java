package stefan.toth.RestAPIProject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date created_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("Modified_at")
    private Date modified_at;
}
