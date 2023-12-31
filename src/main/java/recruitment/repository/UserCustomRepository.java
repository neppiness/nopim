package recruitment.repository;

import java.util.Optional;
import recruitment.domain.User;

public interface UserCustomRepository {

    Optional<User> findByNameAndPassword(String name, String password);

    Optional<User> findByName(String name);

}

