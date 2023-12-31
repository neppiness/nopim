package recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recruitment.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
}
