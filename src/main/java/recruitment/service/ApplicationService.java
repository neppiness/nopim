package recruitment.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recruitment.domain.Application;
import recruitment.domain.User;
import recruitment.dto.ApplicationResponse;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;

    public List<ApplicationResponse> getByUsername(String username) {
        Optional<User> mayBeFoundUser = userRepository.findByName(username);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();

        return foundUser
                .getApplications()
                .stream()
                .map(Application::convertToResponse)
                .toList();
    }

}
