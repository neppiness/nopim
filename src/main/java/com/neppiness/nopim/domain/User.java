package com.neppiness.nopim.domain;

import com.neppiness.nopim.dto.UserResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user")
    private List<Application> applications = new ArrayList<>();

    @Builder
    public User(final Long id, final String name, final String password, final Authority authority,
                final List<Application> applications) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authority = authority;
        this.applications = initializeApplications(applications);
    }

    public UserResponse toResponse() {
        return UserResponse.builder()
                .name(this.name)
                .authority(this.authority)
                .build();
    }

    private List<Application> initializeApplications(List<Application> applications) {
        if (applications == null) {
            return new ArrayList<>();
        }
        return applications;
    }

}
