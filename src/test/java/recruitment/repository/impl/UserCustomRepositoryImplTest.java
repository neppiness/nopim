package recruitment.repository.impl;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import recruitment.domain.User;
import recruitment.repository.UserRepository;

@Sql(value = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "classpath:data/init.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class UserCustomRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저명, 비밀번호로 조회 기능 테스트")
    @Test
    void findByNameAndPasswordTest() {
        String name = "scsc3204";
        String password = "Neppiness12!";
        Optional<User> mayBeFoundUser = userRepository.findByNameAndPassword(name, password);
        assert mayBeFoundUser.isPresent();
        User foundUser = mayBeFoundUser.get();
        Assertions
                .assertThat(foundUser.getName())
                .isEqualTo(name);
        Assertions
                .assertThat(foundUser.getPassword())
                .isEqualTo(password);
    }

    @DisplayName("유저명으로 조회하는 기능 테스트")
    @Test
    void findByNameTest() {
        String name = "scsc3204";
        Optional<User> mayBeFoundUser = userRepository.findByName(name);
        assert mayBeFoundUser.isPresent();
        User foundUser = mayBeFoundUser.get();
        Assertions
                .assertThat(foundUser.getName())
                .isEqualTo(name);
    }

}