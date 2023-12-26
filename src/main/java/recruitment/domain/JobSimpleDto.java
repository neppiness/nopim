package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JobSimpleDto {

    @JsonProperty("채용공고_id")
    private final long id;

    @JsonProperty("회사명")
    private final String companyName;

    @JsonProperty("국가")
    private final String country;

    @JsonProperty("지역")
    private final String region;

    @JsonProperty("채용포지션")
    private final String position;

    @JsonProperty("채용보상금")
    private final long bounty;

    @JsonProperty("사용기술")
    private final String stack;

    @Builder
    public JobSimpleDto(final long id, final String companyName, final String country, final String region,
                        final String position, final long bounty, final String stack) {
        this.id = id;
        this.companyName = companyName;
        this.country = country;
        this.region = region;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
    }

}
