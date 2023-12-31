package recruitment.repository.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import recruitment.domain.Authority;
import recruitment.domain.User;
import recruitment.repository.UserRepository;

@Transactional
@SpringBootTest
class UserCustomRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저명, 비밀번호로 조회 기능 테스트")
    @Test
    void findByNameAndPasswordTest() {
        String name = "KJH";
        String password = "1234";
        User user = User.builder()
                .name(name)
                .password(password)
                .authority(Authority.MEMBER)
                .build();
        User savedUser = userRepository.save(user);
        Optional<User> mayBeFoundUser = userRepository.findByNameAndPassword(name, password);
        assert mayBeFoundUser.isPresent();
        User foundUser = mayBeFoundUser.get();
        Assertions
                .assertThat(savedUser)
                .isEqualTo(foundUser);
    }

    @DisplayName("유저명으로 조회하는 기능 테스트")
    @Test
    void findByNameTest() {
        String name = "KJH";
        String password = "1234";
        User user = User.builder()
                .name(name)
                .password(password)
                .authority(Authority.MEMBER)
                .build();
        User savedUser = userRepository.save(user);
        Optional<User> mayBeFoundUser = userRepository.findByName(name);
        assert mayBeFoundUser.isPresent();
        User foundUser = mayBeFoundUser.get();
        Assertions
                .assertThat(savedUser)
                .isEqualTo(foundUser);
    }

}