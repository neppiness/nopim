package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @JsonProperty("지원내역_id")
    public long getId() {
        return id;
    }

    @JsonProperty("사용자_id")
    public User getUser() {
        return user;
    }

    @JsonProperty("채용공고_id")
    public Job getJobId() {
        return job;
    }

    public ApplicationDto convertToDto() {
        ApplicationDto dto = new ApplicationDto();
        dto.setUserId(this.user.getId());
        dto.setJobId(this.job.getId());
        return dto;
    }

    public ApplicationDetailedDto convertToDetailedDto() {
        ApplicationDetailedDto detailedDto = new ApplicationDetailedDto();
        detailedDto.setUser(this.user);
        detailedDto.setJobSimpleDto(this.job.convertToJobSimpleDto());
        return detailedDto;
    }

}
