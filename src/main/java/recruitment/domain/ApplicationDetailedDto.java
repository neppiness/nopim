package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class ApplicationDetailedDto {

    private final User user;

    private final JobSimpleDto jobSimpleDto;

    @Builder
    public ApplicationDetailedDto(final User user, final JobSimpleDto jobSimpleDto) {
        this.user = user;
        this.jobSimpleDto = jobSimpleDto;
    }

    @JsonProperty("사용자_id")
    public long getUserId() {
        return user.getId();
    }

    @JsonProperty("사용자명")
    public String getUserName() {
        return user.getName();
    }

    @JsonProperty("채용공고_id")
    public long getJobId() {
        return jobSimpleDto.getId();
    }

    @JsonProperty("회사명")
    public String getCompanyName() {
        return jobSimpleDto.getCompanyName();
    }

    @JsonProperty("국가")
    public String getCountry() {
        return jobSimpleDto.getCountry();
    }

    @JsonProperty("지역")
    public String getRegion() {
        return jobSimpleDto.getRegion();
    }

    @JsonProperty("채용포지션")
    public String getPosition() {
        return jobSimpleDto.getPosition();
    }

    @JsonProperty("채용보상금")
    public long getBounty() {
        return jobSimpleDto.getBounty();
    }

    @JsonProperty("사용기술")
    public String getStack() {
        return jobSimpleDto.getStack();
    }

}
