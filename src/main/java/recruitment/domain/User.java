package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @JsonProperty("사용자_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonProperty("사용자_이름")
    private String name;

    @JsonProperty("사용자_비밀번호")
    private String password;

    @JsonProperty("사용자_권한")
    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @JsonProperty("사용자_지원내역")
    @OneToMany(mappedBy = "user")
    private List<Application> applications = new ArrayList<>();

    @Builder
    public User(final Long id, final String name, final String password, final Authority authority,
                final List<Application> applications) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authority = authority;
        this.applications = initializeApplications(applications);
    }

    private List<Application> initializeApplications(List<Application> applications) {
        if (applications == null) {
            return new ArrayList<>();
        }
        return applications;
    }

}
