package recruitment.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Company;
import recruitment.dto.CompanyRequest;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Company create(CompanyRequest companyRequest) {
        Company createdCompany = Company.builder()
                .name(companyRequest.getName())
                .country(companyRequest.getCountry())
                .region(companyRequest.getRegion())
                .build();
        return companyRepository.save(createdCompany);
    }

    public List<Company> search(CompanyRequest companyRequest) {
        String name = companyRequest.getName();
        String region = companyRequest.getRegion();
        String country = companyRequest.getCountry();
        return companyRepository.findByParameters(name, region, country);
    }

    public Company getDetail(Long companyId) {
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        if (mayBeFoundCompany.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.COMPANY_NOT_FOUND);
        }
        return mayBeFoundCompany.get();
    }

}
