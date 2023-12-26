package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @JsonProperty("사용자_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @JsonProperty("사용자_이름")
    private String name;

    @Builder
    public User(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

}
