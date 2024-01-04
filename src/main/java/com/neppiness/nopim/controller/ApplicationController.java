package com.neppiness.nopim.controller;

import com.neppiness.nopim.dto.ApplicationResponse;
import com.neppiness.nopim.dto.Principal;
import com.neppiness.nopim.dto.PrincipalDto;
import com.neppiness.nopim.service.ApplicationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/applications")
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping(value = "")
    public ResponseEntity<List<ApplicationResponse>> getAllApplicationsOfUser(@Principal PrincipalDto principal) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationService.getAllByUsername(principal.getName()));
    }

}
