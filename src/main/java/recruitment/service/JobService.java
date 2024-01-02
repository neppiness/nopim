package recruitment.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Application;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.Status;
import recruitment.domain.User;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.JobRequest;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    private final ApplicationRepository applicationRepository;

    @Transactional
    public Job create(JobRequest jobRequest) {
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
        return savedJob;
    }

    @Transactional
    public Job update(Long jobId, JobRequest jobRequest) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        Job foundJob = mayBeFoundJob.get();
        JobRequest jobRequestToBeUpdated = getJobRequestToBeUpdated(foundJob, jobRequest);
        Job jobToBeUpdated = Job.builder()
                .id(foundJob.getId())
                .company(foundJob.getCompany())
                .position(jobRequestToBeUpdated.getPosition())
                .bounty(jobRequestToBeUpdated.getBounty())
                .stack(jobRequestToBeUpdated.getStack())
                .description(jobRequestToBeUpdated.getDescription())
                .status(jobRequestToBeUpdated.getStatus())
                .build();

        Job uploadedJob = jobRepository.save(jobToBeUpdated);
        Company companyToBeUploaded = foundJob.getCompany();
        companyToBeUploaded.getJobs().add(uploadedJob);
        companyRepository.save(companyToBeUploaded);
        return uploadedJob;
    }

    @Transactional
    public Job softDelete(Long jobId) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
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

        return uploadedJob;
    }

    public List<JobSimpleResponse> getAll() {
        return jobRepository.findAll()
                .stream()
                .map(Job::convertToJobSimpleResponse)
                .toList();
    }

    public List<JobSimpleResponse> search(String keyword) {
        return jobRepository.findByKeyword(keyword);
    }

    public JobResponse getDetail(Long jobId) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        return mayBeFoundJob.get().convertToJobResponse();
    }

    @Transactional
    public ApplicationResponse apply(Long jobId, String name) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
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
        return savedApplication.convertToResponse();
    }

    private JobRequest getJobRequestToBeUpdated(Job foundJob, JobRequest jobRequest) {
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
        Status status = jobRequest.getStatus();
        if (status == null) {
            status = foundJob.getStatus();
        }
        return JobRequest.builder()
                .position(position)
                .bounty(bounty)
                .description(description)
                .stack(stack)
                .status(status)
                .build();
    }

}
