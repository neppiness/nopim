package recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recruitment.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyCustomRepository {
}
