package demo.backend.tuto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import demo.backend.tuto.demo.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
}
