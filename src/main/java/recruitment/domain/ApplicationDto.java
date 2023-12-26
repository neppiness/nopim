package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class ApplicationDto {

    private final Long userId;

    private final Long jobId;

    @Builder
    public ApplicationDto(long userId, long jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }

    @JsonProperty("사용자_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("채용공고_id")
    public long getJobId() {
        return jobId;
    }

}
