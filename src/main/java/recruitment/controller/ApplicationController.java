package recruitment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recruitment.dto.ApplicationResponse;
import recruitment.service.ApplicationService;

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
