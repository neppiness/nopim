package recruitment.repository;

import org.springframework.data.repository.CrudRepository;
import recruitment.domain.Company;

public interface CompanyRepository extends CrudRepository<Company, Long> {
}
