package recruitment.controller;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.*;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

import java.util.*;

@RestController
@RequestMapping(path="/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    @GetMapping(path="/{userId}/find/{jobId}")
    public ResponseEntity<ApplicationDto> findApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new NoSuchElementException();
        }
        ApplicationDto foundApplicationDto = mayBeFoundApplication.get().convertToDto();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundApplicationDto);
    }

    @GetMapping(path="/{userId}/detail/{jobId}")
    public ResponseEntity<ApplicationDetailedDto> findDetailedApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new NoSuchElementException();
        }
        ApplicationDetailedDto foundDetailedDto = mayBeFoundApplication.get().convertToDetailedDto();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundDetailedDto);
    }

    @PostMapping(path="/{userId}/add")
    public ResponseEntity<ApplicationDto> addApplication(
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
        application = applicationRepository.save(application);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(application.convertToDto());
    }

    @GetMapping(path="/{userId}/all")
    public ResponseEntity<Collection<ApplicationDto>> findApplicationsByUserId(
            @PathVariable Long userId
    ) {
        Collection<Application> foundApplications = applicationRepository.findApplicationsByUserId(userId);
        List<ApplicationDto> foundApplicationDtoList = foundApplications.stream()
                .map(Application::convertToDto)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundApplicationDtoList);
    }

    @DeleteMapping(path="/{userId}/delete/{jobId}")
    public ResponseEntity<String> deleteApplicationByUserIdAndJobId(
            @PathVariable Long userId,
            @PathVariable Long jobId
    ) {
        Optional<Application> mayBeFoundApplication = applicationRepository.findApplicationByUserIdAndJobId(userId, jobId);
        if (mayBeFoundApplication.isEmpty()) {
            throw new NoSuchElementException();
        }
        applicationRepository.delete(mayBeFoundApplication.get());
        String message = "The application data is deleted (userId: " + userId + ", jobId: " + jobId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

    @Transactional
    @DeleteMapping(path="/{userId}/delete/all")
    public ResponseEntity<String> deleteAllApplicationsByUserId(
            @PathVariable Long userId
    ) {
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        applicationRepository.deleteApplicationsByUserId(userId);
        String message = "All application data of the user is deleted (userId : "+ userId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }
}
