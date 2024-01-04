package com.neppiness.nopim.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorityTest {

    @DisplayName(value = "Authority 파싱 테스트: member")
    @Test
    void parseAuthorityTest1() {
        String member = "member";
        Authority authority = Authority.parseAuthority(member);
        Assertions
                .assertThat(authority)
                .isEqualTo(Authority.MEMBER);
    }

    @DisplayName(value = "Authority 파싱 테스트: admin")
    @Test
    void parseAuthorityTest2() {
        String admin = "aDMin";
        Authority authority = Authority.parseAuthority(admin);
        Assertions
                .assertThat(authority)
                .isEqualTo(Authority.ADMIN);
    }

    @DisplayName(value = "Authority 파싱 테스트: manager")
    @Test
    void parseAuthorityTest3() {
        String manager = "MANAGER";
        Authority authority = Authority.parseAuthority(manager);
        Assertions
                .assertThat(authority)
                .isEqualTo(Authority.MANAGER);
    }

    @DisplayName(value = "Authority 파싱 예외 테스트")
    @Test
    void parseAuthorityExceptionTest() {
        String unknown = "neppiness";
        Authority authority = Authority.parseAuthority(unknown);
        Assertions
                .assertThat(authority)
                .isNull();
    }

}