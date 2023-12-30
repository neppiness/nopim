package recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.*;
import recruitment.dto.ApplicationResponse;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.UserRepository;

import java.util.*;

@RequestMapping(path = "/applications")
@RequiredArgsConstructor
@RestController
public class ApplicationController {

    private final UserRepository userRepository;

    @GetMapping(value = "")
    public ResponseEntity<List<ApplicationResponse>> getByUsername(@RequestParam String name)
            throws JsonProcessingException {
        Optional<User> mayBeFoundUser = userRepository.findByName(name);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();

        List<ApplicationResponse> applicationResponseList = foundUser
                .getApplications()
                .stream()
                .map(Application::convertToResponse)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationResponseList);
    }

}
