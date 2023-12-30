package recruitment.repository;

import java.util.List;
import recruitment.domain.Company;

public interface CompanyCustomRepository {

    List<Company> findByParameters(String name, String region, String country);

}
