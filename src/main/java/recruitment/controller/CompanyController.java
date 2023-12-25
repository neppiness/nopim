package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Company;
import recruitment.repository.CompanyRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping(path="/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;

    @PostMapping(path="/add")
    public @ResponseBody Company addCompany(
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String region
    ) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setRegion(region);
        companyRepository.save(company);
        return company;
    }

    @GetMapping(path="/{companyId}")
    public @ResponseBody Company findCompanyById(
            @PathVariable long companyId
    ) {
        Optional<Company> foundCompany = companyRepository.findById(companyId);
        if (foundCompany.isEmpty()) {
            throw new NoSuchElementException();
        }
        return foundCompany.get();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @DeleteMapping(path="/{companyId}")
    public @ResponseBody String deleteCompanyById(
            @PathVariable long companyId
    ) {
        Optional<Company> foundCompany = companyRepository.findById(companyId);
        if (foundCompany.isEmpty()) {
            throw new NoSuchElementException();
        }
        companyRepository.deleteById(companyId);
        return "The company is deleted (companyId: " + companyId + ")";
    }

    @DeleteMapping(path="/all")
    public @ResponseBody String deleteAllCompanies() {
        companyRepository.deleteAll();
        return "All company data deleted";
    }
}
