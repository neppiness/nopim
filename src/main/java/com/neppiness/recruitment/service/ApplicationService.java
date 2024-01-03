package com.neppiness.recruitment.service;

import com.neppiness.recruitment.domain.Application;
import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.dto.ApplicationResponse;
import com.neppiness.recruitment.exception.ResourceNotFoundException;
import com.neppiness.recruitment.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;

    public List<ApplicationResponse> getByUsername(String username) {
        Optional<User> mayBeFoundUser = userRepository.findByName(username);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();

        return foundUser
                .getApplications()
                .stream()
                .map(Application::convertToResponse)
                .toList();
    }

}
