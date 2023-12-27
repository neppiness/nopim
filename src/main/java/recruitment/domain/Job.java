package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;

@Getter
@NoArgsConstructor
@Entity
public class Job {

    @JsonProperty("채용공고_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @JsonProperty("회사")
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @JsonProperty("채용포지션")
    private String position;

    @JsonProperty("채용보상금")
    private long bounty;

    @JsonProperty("사용기술")
    private String stack;

    @JsonProperty("채용내용")
    private String description;

    @Builder
    public Job(final long id, final Company company, final String position, final long bounty, final String stack,
               final String description) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
        this.description = description;
    }

    public boolean hasKeywordInAttributes(String keyword) {
        if (String.valueOf(this.id).contains(keyword)) {
            return true;
        }
        if (this.position.contains(keyword)) {
            return true;
        }
        if (this.stack.contains(keyword)) {
            return true;
        }
        if (this.description.contains(keyword)) {
            return true;
        }
        if (this.getCompany().getName().contains(keyword)) {
            return true;
        }
        return false;
    }

    public JobResponse convertToJobResponse() {
        Set<Long> jobIdList = new HashSet<>();
        Set<Job> companyJobs = this.company.getJobs();
        if (companyJobs == null) {
            companyJobs = new HashSet<>();
        }
        companyJobs.forEach(job -> {
            long jobId = job.getId();
            if (jobId == this.id) {
                return;
            }
            jobIdList.add(job.getId());
        });

        return JobResponse.builder()
                .id(this.id)
                .companyName(this.company.getName())
                .country(this.company.getCountry())
                .region(this.company.getRegion())
                .position(this.position)
                .bounty(this.bounty)
                .stack(this.stack)
                .description(this.description)
                .otherJobIdsOfCompany(jobIdList)
                .build();
    }

    public JobSimpleResponse convertToJobSimpleResponse() {
        return JobSimpleResponse.builder()
                .id(this.id)
                .companyName(this.company.getName())
                .country(this.company.getCountry())
                .region(this.company.getRegion())
                .position(this.position)
                .bounty(this.bounty)
                .stack(this.stack)
                .build();
    }

}