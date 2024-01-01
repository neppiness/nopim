package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;

@Getter
@NoArgsConstructor
@Entity
public class Job {

    @JsonProperty("채용공고_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonProperty("회사")
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @JsonProperty("채용포지션")
    private String position;

    @JsonProperty("채용보상금")
    private Long bounty;

    @JsonProperty("사용기술")
    private String stack;

    @JsonProperty("채용내용")
    private String description;

    @JsonProperty("상태")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Job(final Long id, final Company company, final String position, final Long bounty, final String stack,
               final String description, final Status status) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
        this.description = description;
        this.status = status;
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
        List<Long> jobIdList = new ArrayList<>();
        List<Job> companyJobs = this.company.getJobs();
        if (companyJobs == null) {
            companyJobs = new ArrayList<>();
        }
        companyJobs.forEach(job -> {
            Long jobId = job.getId();
            if (Objects.equals(jobId, this.id)) {
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