package recruitment.controller;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.*;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/application")
public class ApplicationController {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(path="/{userId}/find/{jobId}")
    public @ResponseBody ApplicationDto findApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new NoSuchElementException();
        }
        return mayBeFoundApplication.get().convertToDto();
    }

    @GetMapping(path="/{userId}/detail/{jobId}")
    public @ResponseBody ApplicationDetailedDto findDetailedApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new NoSuchElementException();
        }
        return mayBeFoundApplication.get().convertToDetailedDto();
    }

    @PostMapping(path="/{userId}/add")
    public @ResponseBody ApplicationDto addApplication(
            @PathVariable Long userId,
            @RequestParam Long jobId
    ) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new NoSuchElementException();
        }
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isPresent()) {
            throw new DuplicateRequestException();
        }
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        Application application = new Application();
        application.setUser(mayBeFoundUser.get());
        application.setJob(mayBeFoundJob.get());
        applicationRepository.save(application);
        return application.convertToDto();
    }

    @GetMapping(path="/{userId}/all")
    public @ResponseBody Collection<ApplicationDto> findApplicationsByUserId(
            @PathVariable Long userId
    ) {
        Collection<Application> foundApplications = applicationRepository.findApplicationsByUserId(userId);
        return foundApplications.stream().map(Application::convertToDto).collect(Collectors.toList());
    }

    @DeleteMapping(path="/{userId}/delete/{jobId}")
    public @ResponseBody String deleteApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new NoSuchElementException();
        }
        applicationRepository.delete(mayBeFoundApplication.get());
        return "The application data is deleted (userId: " + userId + ", jobId: " + jobId + ")";
    }

    @Transactional
    @DeleteMapping(path="/{userId}/delete/all")
    public @ResponseBody String deleteAllApplicationsByUserId(
            @PathVariable Long userId
    ) {
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        applicationRepository.deleteApplicationsByUserId(userId);
        return "All application data of the user is deleted (userId : "+ userId + ")";
    }
}
