package recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recruitment.domain.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {
}