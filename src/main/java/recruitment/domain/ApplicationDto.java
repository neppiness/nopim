package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class ApplicationDto {

    private long jobId;

    private long userId;

    @JsonProperty("채용공고_id")
    public long getJobId() {
        return jobId;
    }

    @JsonProperty("사용자_id")
    public long getUserId() {
        return userId;
    }
}

