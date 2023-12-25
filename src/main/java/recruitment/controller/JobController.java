package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.JobDto;
import recruitment.domain.JobSimpleDto;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;

import java.util.*;

@RestController
@RequestMapping(path="/job")
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;

    private final CompanyRepository companyRepository;

    @PostMapping(path="/add")
    public ResponseEntity<JobSimpleDto> addJob (
            @RequestParam Long companyId,
            @RequestParam String position,
            @RequestParam Long bounty,
            @RequestParam String stack,
            @RequestParam String description
    ) {
        Job job = new Job();
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        if (mayBeFoundCompany.isEmpty()) {
            String msg = "The company not found (companyId: " + companyId + ")";
            throw new Error(msg);
        }
        job.setCompany(mayBeFoundCompany.get());
        job.setPosition(position);
        job.setBounty(bounty);
        job.setStack(stack);
        job.setDescription(description);

        jobRepository.save(job);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(job.convertToJobSimpleDto());
    }

    @GetMapping(path="/search")
    public ResponseEntity<Iterable<JobSimpleDto>> searchJob(
            @RequestParam String keyword
    ) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<JobSimpleDto> foundJobs = new ArrayList<>();

        allJobs.iterator().forEachRemaining(job -> {
            if (job.hasKeywordInAttributes(keyword)) {
                foundJobs.add(job.convertToJobSimpleDto());
            }
        });
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundJobs);
    }

    @GetMapping(path="/detail/{jobId}")
    public ResponseEntity<JobDto> getJobDetail(
            @PathVariable Long jobId
    ) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new NoSuchElementException();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mayBeFoundJob.get().convertToJobDto());
    }

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<JobSimpleDto>> findAllJobs() {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<JobSimpleDto> jobs = new ArrayList<>();
        for (Job job : allJobs) {
            jobs.add(job.convertToJobSimpleDto());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobs);
    }

    @PutMapping(path="/update/{jobId}")
    public ResponseEntity<JobSimpleDto> updateJob(
            @PathVariable Long jobId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) Long bounty,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String stack
    ) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new NoSuchElementException();
        }
        Job foundJob = mayBeFoundJob.get();
        if (position != null) foundJob.setPosition(position);
        if (bounty != null) foundJob.setBounty(bounty);
        if (description != null) foundJob.setDescription(description);
        if (stack != null) foundJob.setStack(stack);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundJob.convertToJobSimpleDto());
    }

    @DeleteMapping(path="/delete/{jobId}")
    public ResponseEntity<String> deleteJobById(
            @PathVariable Long jobId
    ) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new NoSuchElementException();
        }
        jobRepository.deleteById(jobId);
        String message = "The job is deleted (jobId: " + jobId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

    @DeleteMapping(path="/delete/all")
    public ResponseEntity<String> deleteAllJobs() {
        jobRepository.deleteAll();
        String message = "All job data deleted";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }
}