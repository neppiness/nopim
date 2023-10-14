package recruitment.repository;

import org.springframework.data.repository.CrudRepository;
import recruitment.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
