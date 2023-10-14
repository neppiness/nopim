package recruitment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.JobDto;
import recruitment.domain.JobSimpleDto;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;

import java.util.*;

@Controller
@RequestMapping(path="/job")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping(path="/add")
    public @ResponseBody JobSimpleDto addJob (
            @RequestParam Long companyId,
            @RequestParam String position,
            @RequestParam Long bounty,
            @RequestParam String stack,
            @RequestParam String description
    ) {
        Job job = new Job();
        Optional<Company> foundCompany = companyRepository.findById(companyId);
        if (foundCompany.isEmpty()) {
            String msg = "The company not found (companyId: " + companyId + ")";
            throw new Error(msg);
        }
        job.setCompany(foundCompany.get());
        job.setPosition(position);
        job.setBounty(bounty);
        job.setStack(stack);
        job.setDescription(description);

        jobRepository.save(job);
        return job.convertToJobSimpleDto();
    }

    @GetMapping(path="/search")
    public @ResponseBody Iterable<JobSimpleDto> searchJob(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String stack
    ) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<JobSimpleDto> foundJobs = new ArrayList<>();

        allJobs.iterator().forEachRemaining(job -> {
            if (jobId != null && !Objects.equals(job.getId(), jobId)) return;
            if (companyId != null && !Objects.equals(job.getCompanyId(), companyId)) return;
            if (companyName != null && !job.getCompanyName().equals(companyName)) return;
            if (position != null && !job.getPosition().equals(position)) return;
            if (stack != null && !job.getStack().equals(stack)) return;
            foundJobs.add(job.convertToJobSimpleDto());
        });
        return foundJobs;
    }

    @GetMapping(path="/detail/{job_id}")
    public @ResponseBody JobDto getJobDetail(
            @PathVariable Long jobId
    ) {
        Optional<Job> foundJob = jobRepository.findById(jobId);
        if (foundJob.isEmpty()) {
            throw new NoSuchElementException();
        }
        return foundJob.get().convertToJobDto();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<JobSimpleDto> findAllJobs() {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<JobSimpleDto> jobs = new ArrayList<>();
        allJobs.forEach(job -> jobs.add(job.convertToJobSimpleDto()));
        return jobs;
    }

    // TODO: 채용공고 수정 메소드 구현

    @DeleteMapping(path="/delete/{job_id}")
    public @ResponseBody String deleteJobById(
            @PathVariable Long jobId
    ) {
        Optional<Job> foundJob = jobRepository.findById(jobId);
        if (foundJob.isEmpty()) {
            throw new NoSuchElementException();
        }
        jobRepository.deleteById(jobId);
        return "The job is deleted (jobId: " + jobId + ")";
    }

    @DeleteMapping(path="/delete/all")
    public @ResponseBody String deleteAllJobs() {
        jobRepository.deleteAll();
        return "All job data deleted";
    }
}