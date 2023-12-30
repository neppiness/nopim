package recruitment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Company;
import recruitment.dto.CompanyRequest;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.CompanyRepository;

import java.util.Optional;

@RequestMapping(path = "/companies")
@RequiredArgsConstructor
@RestController
public class CompanyController {

    private final CompanyRepository companyRepository;

    @Transactional
    @PostMapping(path = "")
    public ResponseEntity<Company> create(@ModelAttribute CompanyRequest companyRequest) {
        Company company = Company.builder()
                .name(companyRequest.getName())
                .country(companyRequest.getCountry())
                .region(companyRequest.getRegion())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyRepository.save(company));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Company>> search(@ModelAttribute CompanyRequest companyRequest) {
        String name = companyRequest.getName();
        String region = companyRequest.getRegion();
        String country = companyRequest.getCountry();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRepository.findByParameters(name, region, country));
    }

    @GetMapping(path = "/detail/{companyId}")
    public ResponseEntity<Company> getDetail(@PathVariable Long companyId) {
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        if (mayBeFoundCompany.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.COMPANY_NOT_FOUND);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mayBeFoundCompany.get());
    }

}
