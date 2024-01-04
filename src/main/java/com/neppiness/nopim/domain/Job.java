package com.neppiness.nopim.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neppiness.nopim.dto.JobResponse;
import com.neppiness.nopim.dto.JobSimpleResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public JobResponse convertToJobResponse() {
        List<Long> jobIdList = this.company.getJobs()
                .stream()
                .map(Job::getId)
                .filter(jobId -> !Objects.equals(jobId, this.id))
                .toList();

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
                .status(this.status)
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
                .status(this.status)
                .build();
    }

}