package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private long id;

    @Column(name = "company_name")
    private String name;

    @Column(name = "company_country")
    private String country;

    @Column(name = "company_region")
    private String region;

    @OneToMany(mappedBy = "company")
    @Column(name = "company_jobs")
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

    @JsonProperty("회사가_올린_다른_채용공고")
    public Set<Job> getJobs() {
        return jobs;
    }
}
