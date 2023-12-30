package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Company {

    @JsonProperty("회사_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @JsonProperty("회사명")
    private String name;

    @JsonProperty("지역")
    private String region;

    @JsonProperty("국가")
    private String country;

    @JsonProperty("회사가_올린_채용공고")
    @OneToMany(mappedBy = "company")
    private Set<Job> jobs;

    @Builder
    public Company(final long id, final String name, final String country, final String region, final Set<Job> jobs) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.region = region;
        this.jobs = jobs;
    }

}
