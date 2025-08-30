package demo.backend.tuto.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.domain.DTO.Meta;
import demo.backend.tuto.demo.domain.DTO.ResultPaginationDTO;
import demo.backend.tuto.demo.domain.DTO.Users.CreatedUserDTO;
import demo.backend.tuto.demo.domain.DTO.Users.FetchUserDTO;
import demo.backend.tuto.demo.domain.DTO.Users.UpdateUserDTO;
import demo.backend.tuto.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleGetUser(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());
        result.setMeta(meta);
        List<FetchUserDTO> fetchUserDTOs = pageUser.getContent().stream()
                .map(item -> new FetchUserDTO(item.getId(), item.getEmail(), item.getUsername(), item.getAddress(),
                        item.getAge(), item.getGender(), item.getUpdatedAt(), item.getCreatedAt()))
                .collect(Collectors.toList());
        result.setResult(fetchUserDTOs);
        return result;
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.handleGetUser(user.getId());
        if (currentUser != null) {
            currentUser.setUsername(user.getUsername());
            currentUser.setEmail(user.getEmail());
            currentUser.setAddress(user.getAddress());
            currentUser.setAge(user.getAge());
            currentUser.setGender(user.getGender());
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User findUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public CreatedUserDTO convertUserDTO(User newUser) {
        CreatedUserDTO createdUserDTO = new CreatedUserDTO();
        createdUserDTO.setId(newUser.getId());
        createdUserDTO.setEmail(newUser.getEmail());
        createdUserDTO.setUsername(newUser.getUsername());
        createdUserDTO.setAddress(newUser.getAddress());
        createdUserDTO.setAge(newUser.getAge());
        createdUserDTO.setGender(newUser.getGender());
        createdUserDTO.setCreatedAt(newUser.getCreatedAt());
        return createdUserDTO;
    }

    public FetchUserDTO convertFetchUserDTO(User user) {
        FetchUserDTO fetchUserDTO = new FetchUserDTO();
        fetchUserDTO.setId(user.getId());
        fetchUserDTO.setEmail(user.getEmail());
        fetchUserDTO.setUsername(user.getUsername());
        fetchUserDTO.setAddress(user.getAddress());
        fetchUserDTO.setAge(user.getAge());
        fetchUserDTO.setGender(user.getGender());
        fetchUserDTO.setUpdatedAt(user.getUpdatedAt());
        fetchUserDTO.setCreatedAt(user.getCreatedAt());
        return fetchUserDTO;
    }

    public UpdateUserDTO convertUpdateUserDTO(User user) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(user.getId());
        updateUserDTO.setEmail(user.getEmail());
        updateUserDTO.setUsername(user.getUsername());
        updateUserDTO.setAddress(user.getAddress());
        updateUserDTO.setAge(user.getAge());
        updateUserDTO.setGender(user.getGender());
        updateUserDTO.setUpdatedAt(user.getUpdatedAt());
        return updateUserDTO;
    }

    public void updateUserToken(String token, String email) {
        User user = this.findUserByUsername(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

}
