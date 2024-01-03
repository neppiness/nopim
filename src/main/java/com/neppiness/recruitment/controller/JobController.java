package com.neppiness.recruitment.controller;

import com.neppiness.recruitment.domain.Job;
import com.neppiness.recruitment.dto.ApplicationResponse;
import com.neppiness.recruitment.dto.JobRequest;
import com.neppiness.recruitment.dto.JobResponse;
import com.neppiness.recruitment.dto.JobSimpleResponse;
import com.neppiness.recruitment.dto.Principal;
import com.neppiness.recruitment.dto.PrincipalDto;
import com.neppiness.recruitment.service.AuthorizationService;
import com.neppiness.recruitment.service.JobService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/jobs")
@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private final AuthorizationService authorizationService;

    @PostMapping(path = "")
    public ResponseEntity<Job> create(@Principal PrincipalDto principal, @ModelAttribute JobRequest jobRequest) {
        authorizationService.checkIfManager(principal.getAuthority());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.create(jobRequest));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Job> update(@Principal PrincipalDto principal, @PathVariable Long id,
                                      @ModelAttribute JobRequest jobRequest) {
        authorizationService.checkIfManager(principal.getAuthority());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.update(id, jobRequest));
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Job> softDelete(@Principal PrincipalDto principal, @PathVariable Long id) {
        authorizationService.checkIfManager(principal.getAuthority());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.softDelete(id));
    }

    @GetMapping(path = "")
    public ResponseEntity<List<JobSimpleResponse>> getAll(@Principal PrincipalDto principal) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.getAll());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<JobSimpleResponse>> search(@Principal PrincipalDto principal,
                                                          @RequestParam String keyword) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.search(keyword));
    }

    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<JobResponse> getDetail(@Principal PrincipalDto principal, @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.getDetail(id));
    }

    @PostMapping(path = "/apply/{id}")
    public ResponseEntity<ApplicationResponse> apply(@Principal PrincipalDto principal, @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.apply(id, principal.getName()));
    }

}