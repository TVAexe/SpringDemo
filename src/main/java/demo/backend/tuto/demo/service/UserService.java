package demo.backend.tuto.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.Company;
import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.domain.response.CreatedUserDTO;
import demo.backend.tuto.demo.domain.response.FetchUserDTO;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.domain.response.UpdateUserDTO;
import demo.backend.tuto.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User user) {
        if (user.getCompany() != null) {
            Company companyOptional = this.companyService.handleGetCompany(user.getCompany().getId());
            if (companyOptional != null) {
                user.setCompany(companyOptional);
            } else {
                user.setCompany(null);
            }
        }
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
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());
        result.setMeta(meta);
        List<FetchUserDTO> fetchUserDTOs = pageUser.getContent().stream()
    .map(item -> new FetchUserDTO(
        item.getId(),
        item.getName(),
        item.getEmail(),
        item.getAddress(),
        item.getAge(),
        item.getGender(),
        item.getUpdatedAt(),
        item.getCreatedAt(),
        new FetchUserDTO.CompanyUser(
            (item.getCompany() != null ? item.getCompany().getId() : 0),
            (item.getCompany() != null ? item.getCompany().getName() : null)
        )
    ))
    .collect(Collectors.toList());
        result.setResult(fetchUserDTOs);
        return result;
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.handleGetUser(user.getId());
        if (currentUser != null) {
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setAddress(user.getAddress());
            currentUser.setAge(user.getAge());
            currentUser.setGender(user.getGender());
            if (user.getCompany() != null) {
                Company companyOptional = this.companyService.handleGetCompany(user.getCompany().getId());
                if (companyOptional != null) {
                    currentUser.setCompany(companyOptional);
                } else {
                    currentUser.setCompany(null);
                }
            } else {
                currentUser.setCompany(null);
            }
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
        CreatedUserDTO.CompanyUser companyUser = new CreatedUserDTO.CompanyUser();
        createdUserDTO.setId(newUser.getId());
        createdUserDTO.setEmail(newUser.getEmail());
        createdUserDTO.setName(newUser.getName());
        createdUserDTO.setAddress(newUser.getAddress());
        createdUserDTO.setAge(newUser.getAge());
        createdUserDTO.setGender(newUser.getGender());
        createdUserDTO.setCreatedAt(newUser.getCreatedAt());
        if (newUser.getCompany() != null) {
            companyUser.setId(newUser.getCompany().getId());
            companyUser.setName(newUser.getCompany().getName());
            createdUserDTO.setCompany(companyUser);
        }
        return createdUserDTO;
    }

    public FetchUserDTO convertFetchUserDTO(User user) {
        FetchUserDTO fetchUserDTO = new FetchUserDTO();
        FetchUserDTO.CompanyUser companyUser = new FetchUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            fetchUserDTO.setCompany(companyUser);
        }
        fetchUserDTO.setId(user.getId());
        fetchUserDTO.setEmail(user.getEmail());
        fetchUserDTO.setName(user.getName());
        fetchUserDTO.setAddress(user.getAddress());
        fetchUserDTO.setAge(user.getAge());
        fetchUserDTO.setGender(user.getGender());
        fetchUserDTO.setUpdatedAt(user.getUpdatedAt());
        fetchUserDTO.setCreatedAt(user.getCreatedAt());
        return fetchUserDTO;
    }

    public UpdateUserDTO convertUpdateUserDTO(User user) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        UpdateUserDTO.CompanyUser companyUser = new UpdateUserDTO.CompanyUser();
        updateUserDTO.setEmail(user.getEmail());
        updateUserDTO.setId(user.getId());
        updateUserDTO.setName(user.getName());
        updateUserDTO.setAddress(user.getAddress());
        updateUserDTO.setAge(user.getAge());
        updateUserDTO.setGender(user.getGender());
        updateUserDTO.setUpdatedAt(user.getUpdatedAt());
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            updateUserDTO.setCompany(companyUser);
        }
        return updateUserDTO;
    }

    public void updateUserToken(String token, String email) {
        User user = this.findUserByUsername(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

}
