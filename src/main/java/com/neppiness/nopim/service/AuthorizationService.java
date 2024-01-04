package com.neppiness.nopim.service;

import com.neppiness.nopim.domain.Authority;
import com.neppiness.nopim.exception.AuthorityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    public void checkIfAdmin(Authority authority) {
        if (authority != Authority.ADMIN) {
            throw new AuthorityException(AuthorityException.REQUIRE_ADMIN_AUTHORITY);
        }
    }

    public void checkIfManager(Authority authority) {
        if (authority != Authority.MANAGER) {
            throw new AuthorityException(AuthorityException.REQUIRE_MANAGER_AUTHORITY);
        }
    }

    public void checkIfNotMember(Authority authority) {
        if (authority == Authority.MEMBER) {
            throw new AuthorityException(AuthorityException.NOT_ALLOWED_TO_MEMBER);
        }
    }

}
