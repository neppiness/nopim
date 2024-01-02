package recruitment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import recruitment.domain.Status;

@Getter
public class JobResponse {

    @JsonProperty("채용공고_id")
    private final Long id;

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

    @JsonProperty("채용내용")
    private final String description;

    @JsonProperty("회사가_올린_다른_채용공고")
    private final List<Long> otherJobIdsOfCompany;

    @JsonProperty("상태")
    private final Status status;

    @Builder
    public JobResponse(final Long id, final String companyName, final String country, final String region,
                       final String position, final long bounty, final String stack, final String description,
                       final List<Long> otherJobIdsOfCompany, final Status status) {
        this.id = id;
        this.companyName = companyName;
        this.country = country;
        this.region = region;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
        this.description = description;
        this.otherJobIdsOfCompany = otherJobIdsOfCompany;
        this.status = status;
    }

}
