package recruitment.repository;

import org.springframework.data.repository.CrudRepository;
import recruitment.domain.Job;

public interface JobRepository extends CrudRepository<Job, Long> {

}