package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Company;
import recruitment.repository.CompanyRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;

    @PostMapping(path = "/add")
    public ResponseEntity<Company> addCompany(@RequestParam String name, @RequestParam String country,
                                              @RequestParam String region) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setRegion(region);
        companyRepository.save(company);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(company);
    }

    @GetMapping(path = "/{companyId}")
    public ResponseEntity<Company> findCompanyById(@PathVariable long companyId) {
        Optional<Company> foundCompany = companyRepository.findById(companyId);
        if (foundCompany.isEmpty()) {
            throw new NoSuchElementException();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundCompany.get());
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<Company>> getAllCompanies() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRepository.findAll());
    }

    @DeleteMapping(path = "/{companyId}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable long companyId) {
        Optional<Company> foundCompany = companyRepository.findById(companyId);
        if (foundCompany.isEmpty()) {
            throw new NoSuchElementException();
        }
        companyRepository.deleteById(companyId);
        String message = "The company is deleted (companyId: " + companyId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

    @DeleteMapping(path = "/all")
    public ResponseEntity<String> deleteAllCompanies() {
        companyRepository.deleteAll();
        String message = "All company data deleted";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

}
