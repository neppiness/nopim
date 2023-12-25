package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    private String position;

    private long bounty;

    private String stack;

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

    public String getCompanyName() {
        return company.getName();
    }

    public Long getCompanyId() {
        return company.getId();
    }

    public boolean hasKeywordInAttributes(String keyword) {
        if (String.valueOf(this.id).contains(keyword)) return true;
        if (this.position.contains(keyword)) return true;
        if (this.stack.contains(keyword)) return true;
        if (this.description.contains(keyword)) return true;
        if (this.getCompanyName().contains(keyword)) return true;
        return false;
    }

    public JobDto convertToJobDto() {
        JobDto jobDto = new JobDto();
        jobDto.setId(this.id);

        jobDto.setCompanyName(this.company.getName());
        jobDto.setCountry(this.company.getCountry());
        jobDto.setRegion(this.company.getRegion());

        jobDto.setPosition(this.position);
        jobDto.setBounty(this.bounty);
        jobDto.setStack(this.stack);

        jobDto.setDescription(this.description);

        Set<Long> jobIdList = new HashSet<>();
        if (this.company.getJobs() == null) {
            jobDto.setOtherJobIdsOfCompany(jobIdList);
            return jobDto;
        }
        this.company.getJobs().forEach(job -> {
            long jobId = job.getId();
            if (jobId == this.id) return;
            jobIdList.add(job.getId());
        });
        jobDto.setOtherJobIdsOfCompany(jobIdList);
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