package com.neppiness.nopim.service;

import com.neppiness.nopim.domain.Application;
import com.neppiness.nopim.domain.Company;
import com.neppiness.nopim.domain.Job;
import com.neppiness.nopim.domain.Status;
import com.neppiness.nopim.domain.User;
import com.neppiness.nopim.dto.ApplicationResponse;
import com.neppiness.nopim.dto.JobDetailResponse;
import com.neppiness.nopim.dto.JobRequest;
import com.neppiness.nopim.dto.JobResponse;
import com.neppiness.nopim.exception.ResourceAlreadyExistException;
import com.neppiness.nopim.exception.ResourceNotFoundException;
import com.neppiness.nopim.repository.ApplicationRepository;
import com.neppiness.nopim.repository.CompanyRepository;
import com.neppiness.nopim.repository.JobRepository;
import com.neppiness.nopim.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new ResourceNotFoundException(ResourceNotFoundException.COMPANY_NOT_FOUND);
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
            throw new ResourceNotFoundException(ResourceNotFoundException.JOB_NOT_FOUND);
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
    public Job close(Long jobId) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.JOB_NOT_FOUND);
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

    public List<JobResponse> getAll() {
        return jobRepository.findAll()
                .stream()
                .map(Job::convertToJobResponse)
                .toList();
    }

    public List<JobResponse> search(String keyword) {
        return jobRepository.findByKeyword(keyword);
    }

    public JobDetailResponse getDetail(Long jobId) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.JOB_NOT_FOUND);
        }
        return mayBeFoundJob.get().convertToJobDetailResponse();
    }

    @Transactional
    public ApplicationResponse apply(Long jobId, String name) {
        Optional<User> mayBeFoundUser = userRepository.findByName(name);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();

        List<Application> applicationList = foundUser.getApplications();
        for (Application application : applicationList) {
            if (application.getJob().getId().equals(jobId)) {
                throw new ResourceAlreadyExistException(ResourceAlreadyExistException.APPLICATION_ALREADY_EXIST);
            }
        }

        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.JOB_NOT_FOUND);
        }
        Job foundJob = mayBeFoundJob.get();

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
