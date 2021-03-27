package stefan.toth.RestAPIProject.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Embeddable
@JsonIgnoreProperties(value = {"image"}, allowSetters = true)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    //Photo
    @Lob
    private Byte[] image;
}
