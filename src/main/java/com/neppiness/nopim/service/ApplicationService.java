package com.neppiness.nopim.service;

import com.neppiness.nopim.domain.Application;
import com.neppiness.nopim.domain.User;
import com.neppiness.nopim.dto.ApplicationResponse;
import com.neppiness.nopim.exception.ResourceNotFoundException;
import com.neppiness.nopim.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;

    public List<ApplicationResponse> getAllByUsername(String username) {
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
