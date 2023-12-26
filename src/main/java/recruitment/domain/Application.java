package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import recruitment.dto.ApplicationDetailedDto;
import recruitment.dto.ApplicationDto;

@Getter
@NoArgsConstructor
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("지원내역_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonProperty("사용자_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    @JsonProperty("채용공고_id")
    private Job job;

    @Builder
    public Application(final long id, final User user, final Job job) {
        this.id = id;
        this.user = user;
        this.job = job;
    }

    public ApplicationDto convertToDto() {
        return ApplicationDto.builder()
                .userId(this.user.getId())
                .jobId(this.job.getId())
                .build();
    }

    public ApplicationDetailedDto convertToDetailedDto() {
        return ApplicationDetailedDto.builder()
                .user(this.user)
                .jobSimpleDto(this.job.convertToJobSimpleDto())
                .build();
    }

}
