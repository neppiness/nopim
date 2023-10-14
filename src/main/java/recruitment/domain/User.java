package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @JsonProperty("사용자_id")
    public long getId() {
        return id;
    }

    @JsonProperty("사용자_이름")
    public String getName() {
        return name;
    }
}
