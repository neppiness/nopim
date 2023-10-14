package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.Set;

@Setter
public class JobDto {

    private long id;
    private String companyName;
    private String country;
    private String region;
    private String position;
    private long bounty;
    private String stack;

    private String description;
    private Set<Long> otherJobIdsOfCompany;

    @JsonProperty("채용공고_id")
    public long getId() {
        return id;
    }

    @JsonProperty("회사명")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("국가")
    public String getCountry() {
        return country;
    }

    @JsonProperty("지역")
    public String getRegion() {
        return region;
    }

    @JsonProperty("채용포지션")
    public String getPosition() {
        return position;
    }

    @JsonProperty("채용보상금")
    public long getBounty() {
        return bounty;
    }

    @JsonProperty("사용기술")
    public String getStack() {
        return stack;
    }

    @JsonProperty("채용내용")
    public String getDescription() {
        return stack;
    }

    @JsonProperty("회사가_올린_다른_채용공고")
    public Set<Long> getOtherJobIdsOfCompany() {
        return otherJobIdsOfCompany;
    }
}
