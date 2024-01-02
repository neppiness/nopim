package com.neppiness.recruitment.controller;

import com.neppiness.recruitment.domain.Company;
import com.neppiness.recruitment.dto.CompanyRequest;
import com.neppiness.recruitment.service.CompanyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/companies")
@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping(path = "")
    public ResponseEntity<Company> create(@ModelAttribute CompanyRequest companyRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyService.create(companyRequest));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Company>> search(@ModelAttribute CompanyRequest companyRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyService.search(companyRequest));
    }

    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<Company> getDetail(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyService.getDetail(id));
    }

}
