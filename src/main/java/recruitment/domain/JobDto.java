package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
public class JobDto {

    @JsonProperty("채용공고_id")
    private long id;

    @JsonProperty("회사명")
    private String companyName;

    @JsonProperty("국가")
    private String country;

    @JsonProperty("지역")
    private String region;

    @JsonProperty("채용포지션")
    private String position;

    @JsonProperty("채용보상금")
    private long bounty;

    @JsonProperty("사용기술")
    private String stack;

    @JsonProperty("채용내용")
    private String description;

    @JsonProperty("회사가_올린_다른_채용공고")
    private Set<Long> otherJobIdsOfCompany;

    @Builder
    public JobDto(final long id, final String companyName, final String country, final String region,
                  final String position, final long bounty, final String stack, String description,
                  Set<Long> otherJobIdsOfCompany) {
        this.id = id;
        this.companyName = companyName;
        this.country = country;
        this.region = region;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
        this.description = description;
        this.otherJobIdsOfCompany = otherJobIdsOfCompany;
    }

}
