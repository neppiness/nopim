package recruitment.controller;

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
import recruitment.domain.Job;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.JobRequest;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;
import recruitment.service.JobService;

@RequestMapping(path = "/jobs")
@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping(path = "")
    public ResponseEntity<Job> create(@ModelAttribute JobRequest jobRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.create(jobRequest));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Job> update(@PathVariable Long id, @ModelAttribute JobRequest jobRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.update(id, jobRequest));
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Job> softDelete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.softDelete(id));
    }

    @GetMapping(path = "")
    public ResponseEntity<List<JobSimpleResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.getAll());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<JobSimpleResponse>> search(@RequestParam String keyword) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.search(keyword));
    }

    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<JobResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobService.getDetail(id));
    }

    @PostMapping(path = "/apply/{id}")
    public ResponseEntity<ApplicationResponse> apply(@PathVariable Long id, @RequestParam String name) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.apply(id, name));
    }

}