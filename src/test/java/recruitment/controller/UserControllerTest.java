package recruitment.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Authority;
import recruitment.domain.User;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.UserRepository;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    ApplicationRepository applicationRepository;

    @BeforeEach
    void userDatabaseSetup() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "회원가입 테스트")
    void signUpTest() {
        String name = "KJH";
        String password = "4567";

        User addedUser = userController.signUp(name, password).getBody();
        assert addedUser != null;

        Optional<User> mayBeFoundUser = userRepository.findById(addedUser.getId());
        assert mayBeFoundUser.isPresent();
        User foundUser = mayBeFoundUser.get();
        Assertions
                .assertThat(addedUser)
                .isEqualTo(foundUser);
    }

    @Test
    @DisplayName(value = "로그인 테스트")
    void loginTest() {
        String name = "KJH";
        String password = "4567";

        User addedUser = userController.signUp(name, password).getBody();
        assert addedUser != null;

        User loginInfo = userController.login(name, password).getBody();
        Assertions
                .assertThat(addedUser)
                .isEqualTo(loginInfo);
    }

    @Test
    @DisplayName(value = "권한 상승 테스트")
    void promoteTest() {
        String name = "KJH";
        String password = "4567";
        User addedUser = userController.signUp(name, password).getBody();
        assert addedUser != null;

        userController.promote(addedUser.getId());

        Optional<User> mayBePromotedUser = userRepository.findById(addedUser.getId());
        assert mayBePromotedUser.isPresent();
        User promotedUser = mayBePromotedUser.get();
        Assertions
                .assertThat(promotedUser.getAuthority())
                .isEqualTo(Authority.MANAGER);
    }

}
