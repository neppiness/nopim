package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "job_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "job_company", referencedColumnName = "company_id")
    private Company company;

    @Column(name = "job_position")
    private String position;

    @Column(name = "job_bounty")
    private long bounty;

    @Column(name = "job_stack")
    private String stack;

    @Column(name = "job_description")
    private String description;

    @JsonProperty("채용공고_id")
    public long getId() {
        return id;
    }

    @JsonProperty("채용포지션")
    public String getPosition() {
        return position;
    }

    @JsonProperty("채용보상금")
    public long getBounty() {
        return bounty;
    }

    @JsonProperty("사용기술")
    public String getStack() {
        return stack;
    }

    @JsonProperty("채용내용")
    public String getDescription() {
        return description;
    }

    public JobDto convertToJobDto(Company company) {
        JobDto jobDto = new JobDto();
        jobDto.setId(this.id);

        jobDto.setCompanyName(this.company.getName());
        jobDto.setCountry(this.company.getCountry());
        jobDto.setRegion(this.company.getRegion());

        jobDto.setPosition(this.position);
        jobDto.setBounty(this.bounty);
        jobDto.setStack(this.stack);

        jobDto.setDescription(this.description);

        return jobDto;
    }

    public JobSimpleDto convertToJobSimpleDto() {
        JobSimpleDto jobSimpleDto = new JobSimpleDto();
        jobSimpleDto.setId(this.id);

        jobSimpleDto.setCompanyName(this.company.getName());
        jobSimpleDto.setCountry(this.company.getCountry());
        jobSimpleDto.setRegion(this.company.getRegion());

        jobSimpleDto.setPosition(this.position);
        jobSimpleDto.setBounty(this.bounty);
        jobSimpleDto.setStack(this.stack);
        return jobSimpleDto;
    }
}