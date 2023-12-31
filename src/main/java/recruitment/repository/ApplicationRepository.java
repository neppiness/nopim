package recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recruitment.domain.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}