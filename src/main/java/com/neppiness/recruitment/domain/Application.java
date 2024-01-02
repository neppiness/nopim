package com.neppiness.recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neppiness.recruitment.dto.ApplicationResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("지원내역_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonProperty("사용자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    @JsonProperty("채용공고")
    private Job job;

    @Builder
    public Application(final Long id, final User user, final Job job) {
        this.id = id;
        this.user = user;
        this.job = job;
    }

    public ApplicationResponse convertToResponse() {
        return ApplicationResponse.builder()
                .userId(this.user.getId())
                .jobId(this.job.getId())
                .build();
    }

}
