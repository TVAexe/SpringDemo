package demo.backend.tuto.demo.service;
import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.repository.UserRepository;

@Service
public class UserSevice {

    private final UserRepository userRepository;

    public UserSevice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }
}
