package recruitment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyRequest {

    private final String name;

    private final String country;

    private final String region;

    @Builder
    public CompanyRequest(final String name, final String country, final String region) {
        this.name = name;
        this.country = country;
        this.region = region;
    }

}
