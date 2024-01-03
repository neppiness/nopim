package com.neppiness.recruitment.service;

import com.neppiness.recruitment.domain.Authority;
import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.dto.UserRequest;
import com.neppiness.recruitment.exception.ResourceAlreadyExistException;
import com.neppiness.recruitment.repository.UserRepository;
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

    @DisplayName(value = "회원가입 테스트")
    @Test
    void signUpTest() {
        String name = "KJH";
        String password = "Neppiness12!";
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .password(password)
                .build();
        User createdUser = userService.signUp(userRequest);

        Optional<User> mayBeFoundUser = userRepository.findById(createdUser.getId());
        assert mayBeFoundUser.isPresent();
        User foundUser = mayBeFoundUser.get();
        Assertions
                .assertThat(createdUser)
                .isEqualTo(foundUser);
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
        User loginInfo = userService.login(userRequest);
        Assertions
                .assertThat(loginInfo.getName())
                .isEqualTo(name);
        Assertions
                .assertThat(loginInfo.getPassword())
                .isEqualTo(password);
        Assertions
                .assertThat(loginInfo.getAuthority())
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