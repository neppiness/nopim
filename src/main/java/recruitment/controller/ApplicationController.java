package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.*;
import recruitment.dto.ApplicationDetailResponse;
import recruitment.dto.ApplicationResponse;
import recruitment.domain.Job;
import recruitment.exception.ResourceAlreadyExist;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

import java.util.*;

@RestController
@RequestMapping(path = "/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    @GetMapping(path = "/{userId}/find/{jobId}")
    public ResponseEntity<ApplicationResponse> findApplicationByUserIdAndJobId(@PathVariable Long userId,
                                                                               @PathVariable Long jobId) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId,
                jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.APPLICATION_NOT_FOUND);
        }
        ApplicationResponse foundApplicationResponse = mayBeFoundApplication.get().convertToDto();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundApplicationResponse);
    }

    @GetMapping(path = "/{userId}/detail/{jobId}")
    public ResponseEntity<ApplicationDetailResponse> findDetailedApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId,
                jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.APPLICATION_NOT_FOUND);
        }
        ApplicationDetailResponse foundDetailedDto = mayBeFoundApplication.get().convertToDetailedDto();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundDetailedDto);
    }

    @PostMapping(path = "/{userId}/add")
    public ResponseEntity<ApplicationResponse> addApplication(@PathVariable Long userId, @RequestParam Long jobId) {
        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        if (mayBeFoundJob.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.JOB_NOT_FOUND);
        }
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId,
                jobId);
        if (mayBeFoundApplication.isPresent()) {
            throw new ResourceAlreadyExist(ResourceAlreadyExist.APPLICATION_ALREADY_EXIST);
        }
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        Application application = Application.builder()
                .user(mayBeFoundUser.get())
                .job(mayBeFoundJob.get())
                .build();
        application = applicationRepository.save(application);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(application.convertToDto());
    }

    @GetMapping(path = "/{userId}/all")
    public ResponseEntity<Collection<ApplicationResponse>> findApplicationsByUserId(@PathVariable Long userId) {
        Collection<Application> foundApplications = applicationRepository.findApplicationsByUserId(userId);
        List<ApplicationResponse> foundApplicationResponseList = foundApplications.stream()
                .map(Application::convertToDto)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundApplicationResponseList);
    }

    @DeleteMapping(path = "/{userId}/delete/{jobId}")
    public ResponseEntity<String> deleteApplicationByUserIdAndJobId(@PathVariable Long userId,
                                                                    @PathVariable Long jobId) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId,
                jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.APPLICATION_NOT_FOUND);
        }
        applicationRepository.delete(mayBeFoundApplication.get());
        String message = "The application data is deleted (userId: " + userId + ", jobId: " + jobId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

    @Transactional
    @DeleteMapping(path = "/{userId}/delete/all")
    public ResponseEntity<String> deleteAllApplicationsByUserId(@PathVariable Long userId) {
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        applicationRepository.deleteApplicationsByUserId(userId);
        String message = "All application data of the user is deleted (userId : " + userId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

}
