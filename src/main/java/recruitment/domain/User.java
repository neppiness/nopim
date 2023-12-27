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

    @JsonProperty("사용자_비밀번호")
    private String password;

    @JsonProperty("사용자_비밀번호")
    private Authority authority;

    @Builder
    public User(final long id, final String name, final String password, final Authority authority) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authority = authority;
    }

}
