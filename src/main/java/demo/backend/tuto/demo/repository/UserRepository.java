package demo.backend.tuto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.backend.tuto.demo.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be defined here if needed
    
}
