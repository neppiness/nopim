package com.neppiness.recruitment.service;

import com.neppiness.recruitment.domain.Authority;
import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.dto.PrincipalDto;
import com.neppiness.recruitment.dto.TokenResponse;
import com.neppiness.recruitment.dto.UserRequest;
import com.neppiness.recruitment.dto.UserResponse;
import com.neppiness.recruitment.exception.ResourceAlreadyExistException;
import com.neppiness.recruitment.repository.UserRepository;
import com.neppiness.recruitment.util.TokenDecoder;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:data/init.sql")
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenDecoder tokenDecoder;

    @DisplayName(value = "회원가입 테스트")
    @Test
    void signUpTest() {
        String name = "KJH";
        String password = "Neppiness12!";
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .password(password)
                .build();
        UserResponse userResponse = userService.signUp(userRequest);
        Assertions
                .assertThat(userResponse.getName())
                .isEqualTo(name);
        Assertions
                .assertThat(userResponse.getAuthority())
                .isEqualTo(Authority.MEMBER);
    }

    @DisplayName(value = "중복 회원명 회원가입 테스트")
    @Test
    void signUpExceptionTest() {
        String name = "scsc3204";
        String password = "Neppiness12!";
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .password(password)
                .build();

        Assertions
                .assertThatThrownBy(() -> userService.signUp(userRequest))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage(ResourceAlreadyExistException.USER_ALREADY_EXIST);
    }

    @DisplayName(value = "로그인 테스트")
    @Test
    void loginTest() {
        String name = "scsc3204";
        String password = "Neppiness12!";
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .password(password)
                .build();
        TokenResponse tokenResponse = userService.login(userRequest);
        PrincipalDto principal = tokenDecoder.decodePrincipalDto(tokenResponse.getToken());

        Assertions
                .assertThat(principal.getName())
                .isEqualTo(name);
        Assertions
                .assertThat(principal.getAuthority())
                .isEqualTo(Authority.ADMIN);
    }

    @DisplayName(value = "권한 상승 테스트")
    @Test
    void promoteTest() {
        Long userId = 2L;
        userService.promote(userId);

        Optional<User> mayBePromotedUser = userRepository.findById(userId);
        assert mayBePromotedUser.isPresent();

        User promotedUser = mayBePromotedUser.get();
        Assertions
                .assertThat(promotedUser.getAuthority())
                .isEqualTo(Authority.MANAGER);
    }

}