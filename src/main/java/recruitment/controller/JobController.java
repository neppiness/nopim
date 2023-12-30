package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Application;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.Status;
import recruitment.domain.User;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;
import recruitment.dto.JobRequest;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;

import java.util.*;
import recruitment.repository.UserRepository;

@RequestMapping(path = "/jobs")
@RequiredArgsConstructor
@RestController
public class JobController {

    private final ApplicationRepository applicationRepository;

    private final CompanyRepository companyRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    @Transactional
    @PostMapping(path = "")
    public ResponseEntity<Job> create(@ModelAttribute JobRequest jobRequest) {
        Long companyId = jobRequest.getCompanyId();
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        if (mayBeFoundCompany.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.COMPANY_NOT_FOUND);
        }
        Company foundCompany = mayBeFoundCompany.get();
        Job createdJob = Job.builder()
                .company(foundCompany)
                .position(jobRequest.getPosition())
                .bounty(jobRequest.getBounty())
                .stack(jobRequest.getStack())
                .description(jobRequest.getDescription())
                .status(Status.OPEN)
                .build();
        Job savedJob = jobRepository.save(createdJob);
        foundCompany.getJobs().add(savedJob);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedJob);
    }

    @Transactional
    @PutMapping(path = "/{id}")
    public ResponseEntity<Job> update(@PathVariable Long id, @ModelAttribute JobRequest jobRequest) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(id);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        Job foundJob = mayBeFoundJob.get();

        String position = jobRequest.getPosition();
        if (position == null) {
            position = foundJob.getPosition();
        }
        Long bounty = jobRequest.getBounty();
        if (bounty == null) {
            bounty = foundJob.getBounty();
        }
        String description = jobRequest.getDescription();
        if (description == null) {
            description = foundJob.getDescription();
        }
        String stack = jobRequest.getStack();
        if (stack == null) {
            stack = foundJob.getStack();
        }

        Job jobToBeUpdated = Job.builder()
                .id(foundJob.getId())
                .company(foundJob.getCompany())
                .position(position)
                .bounty(bounty)
                .description(description)
                .stack(stack)
                .build();
        Job uploadedJob = jobRepository.save(jobToBeUpdated);
        Company companyToBeUploaded = uploadedJob.getCompany();
        companyToBeUploaded.getJobs().add(uploadedJob);
        companyRepository.save(companyToBeUploaded);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uploadedJob);
    }

    @Transactional
    @PostMapping(path = "/{id}")
    public ResponseEntity<Job> softDelete(@PathVariable Long id) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(id);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        Job foundJob = mayBeFoundJob.get();
        Job jobToBeUpdated = Job.builder()
                .id(foundJob.getId())
                .company(foundJob.getCompany())
                .position(foundJob.getPosition())
                .bounty(foundJob.getBounty())
                .stack(foundJob.getStack())
                .description(foundJob.getDescription())
                .status(Status.CLOSE)
                .build();
        Job uploadedJob = jobRepository.save(jobToBeUpdated);
        Company companyToBeUploaded = uploadedJob.getCompany();
        companyToBeUploaded.getJobs().add(uploadedJob);
        companyRepository.save(companyToBeUploaded);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uploadedJob);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<JobSimpleResponse>> getAll() {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<JobSimpleResponse> jobSimpleResponseList = new ArrayList<>();
        for (Job job : allJobs) {
            jobSimpleResponseList.add(job.convertToJobSimpleResponse());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSimpleResponseList);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<JobSimpleResponse>> search(@RequestParam String keyword) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobRepository.findByKeyword(keyword));
    }

    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<JobResponse> getDetail(@PathVariable Long id) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(id);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mayBeFoundJob.get().convertToJobResponse());
    }

    @Transactional
    @PostMapping(path = "/apply/{id}")
    public ResponseEntity<ApplicationResponse> apply(@PathVariable Long id, @RequestParam String name) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(id);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        Job foundJob = mayBeFoundJob.get();

        Optional<User> mayBeFoundUser = userRepository.findByName(name);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();

        Application createdApplication = Application.builder()
                .job(foundJob)
                .user(foundUser)
                .build();
        Application savedApplication = applicationRepository.save(createdApplication);
        foundUser.getApplications().add(savedApplication);
        userRepository.save(foundUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedApplication.convertToResponse());
    }

}