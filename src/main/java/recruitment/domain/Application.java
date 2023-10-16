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

    private long userId;

    private long jobId;

    @JsonProperty("지원내역_id")
    public long getId() {
        return id;
    }

    @JsonProperty("사용자_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("채용공고_id")
    public long getJobId() {
        return jobId;
    }

    public ApplicationDto convertToDto() {
        ApplicationDto dto = new ApplicationDto();
        dto.setJobId(this.jobId);
        dto.setUserId(this.userId);
        return dto;
    }
}
