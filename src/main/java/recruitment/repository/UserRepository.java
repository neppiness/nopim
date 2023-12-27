package recruitment.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import recruitment.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByNameAndPassword(String name, String password);

}
