package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String country;

    private String region;

    @OneToMany(mappedBy = "company")
    private Set<Job> jobs;

    @JsonProperty("회사_id")
    public long getId() {
        return id;
    }

    @JsonProperty("회사명")
    public String getName() {
        return name;
    }

    @JsonProperty("국가")
    public String getCountry() {
        return country;
    }

    @JsonProperty("지역")
    public String getRegion() {
        return region;
    }

    @JsonProperty("회사가_올린_채용공고")
    public Set<Job> getJobs() {
        return jobs;
    }

}
