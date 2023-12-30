package recruitment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequest {

    private final String name;

    private final String password;

    @Builder
    public UserRequest(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

}
