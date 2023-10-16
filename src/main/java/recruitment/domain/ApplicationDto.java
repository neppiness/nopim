package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class ApplicationDto {

    private long userId;

    private long jobId;

    @JsonProperty("사용자_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("채용공고_id")
    public long getJobId() {
        return jobId;
    }
}
