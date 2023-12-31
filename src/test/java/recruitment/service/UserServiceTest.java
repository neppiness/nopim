package recruitment.service;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Authority;
import recruitment.domain.User;
import recruitment.dto.UserRequest;
import recruitment.repository.UserRepository;

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
        String password = "4567";
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

    @DisplayName(value = "로그인 테스트")
    @Test
    void loginTest() {
        String name = "KJH";
        String password = "4567";
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .password(password)
                .build();

        User createdUser = userService.signUp(userRequest);
        User loginInfo = userService.login(userRequest);
        Assertions
                .assertThat(createdUser)
                .isEqualTo(loginInfo);
    }

    @DisplayName(value = "권한 상승 테스트")
    @Test
    void promoteTest() {
        String name = "KJH";
        String password = "4567";
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .password(password)
                .build();
        User createdUser = userService.signUp(userRequest);
        userService.promote(createdUser.getId());

        Optional<User> mayBePromotedUser = userRepository.findById(createdUser.getId());
        assert mayBePromotedUser.isPresent();

        User promotedUser = mayBePromotedUser.get();
        Assertions
                .assertThat(promotedUser.getAuthority())
                .isEqualTo(Authority.MANAGER);
    }

}