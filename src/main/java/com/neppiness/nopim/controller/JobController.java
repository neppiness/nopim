package com.neppiness.nopim.controller;

import com.neppiness.nopim.domain.Job;
import com.neppiness.nopim.dto.ApplicationResponse;
import com.neppiness.nopim.dto.JobRequest;
import com.neppiness.nopim.dto.JobDetailResponse;
import com.neppiness.nopim.dto.JobResponse;
import com.neppiness.nopim.dto.Principal;
import com.neppiness.nopim.dto.PrincipalDto;
import com.neppiness.nopim.service.AuthorizationService;
import com.neppiness.nopim.service.JobService;
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
    public ResponseEntity<List<JobResponse>> getAll(@Principal PrincipalDto principal) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.getAll());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<JobResponse>> search(@Principal PrincipalDto principal,
                                                    @RequestParam String keyword) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.search(keyword));
    }

    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<JobDetailResponse> getDetail(@Principal PrincipalDto principal, @PathVariable Long id) {
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