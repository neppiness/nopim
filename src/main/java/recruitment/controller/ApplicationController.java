package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recruitment.dto.ApplicationResponse;
import recruitment.service.ApplicationService;
import java.util.*;

@RequestMapping(path = "/applications")
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping(value = "")
    public ResponseEntity<List<ApplicationResponse>> getByUsername(@RequestParam String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationService.getByUsername(name));
    }

}
