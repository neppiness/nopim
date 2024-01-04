package com.neppiness.nopim.domain;

import com.neppiness.nopim.dto.JobDetailResponse;
import com.neppiness.nopim.dto.JobResponse;
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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    private String position;

    private Long bounty;

    private String stack;

    private String description;

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

    public JobDetailResponse convertToJobDetailResponse() {
        List<Long> jobIdList = this.company.getJobs()
                .stream()
                .map(Job::getId)
                .filter(jobId -> !Objects.equals(jobId, this.id))
                .toList();

        return JobDetailResponse.builder()
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

    public JobResponse convertToJobResponse() {
        return JobResponse.builder()
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