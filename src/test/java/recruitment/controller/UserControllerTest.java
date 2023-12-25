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
import recruitment.domain.User;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.UserRepository;

import java.util.HashMap;
import java.util.NoSuchElementException;
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
        userController.deleteAllUsers();
        userController.addUser("Kim-Seonghyeon");
        userController.addUser("Kim-Jeonghyun");
    }

    @Test
    @DisplayName(value = "유저 등록 및 유저 ID로 유저 검색 테스트")
    void addUserAndFindUserByIDTest() {
        User addedUser = userController.addUser("KJH").getBody();
        Optional<User> mayBeFoundUser = userRepository.findById(addedUser.getId());
        if (mayBeFoundUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        User foundUser = mayBeFoundUser.get();
        Assertions.assertThat(addedUser).isEqualTo(foundUser);
    }

    @Test
    @DisplayName(value = "모든 사용자 조회 테스트")
    void getAllUsersTest() {
        Iterable<User> allUsers = userController.getAllUsers().getBody();
        HashMap<String, Boolean> userNameCheck = new HashMap<>();
        userNameCheck.put("Kim-Seonghyeon", false);
        userNameCheck.put("Kim-Jeonghyun", false);
        allUsers.forEach(user -> {
            System.out.println("user.getName() = " + user.getName());
            userNameCheck.put(user.getName(), true);
        });
        Assertions.assertThat(userNameCheck.values()).doesNotContain(false);
    }

    @Test
    @DisplayName(value = "모든 사용자 제거 테스트")
    void deleteAllUsersTest() {
        String log = userController.deleteAllUsers().getBody();
        System.out.println("log = " + log);
        Iterable<User> allUsers = userController.getAllUsers().getBody();
        Assertions.assertThat(allUsers).isEmpty();
    }

}
