package demo.backend.tuto.demo.service;

import demo.backend.tuto.demo.domain.Permission;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.Role;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.repository.PermissionRepository;
import demo.backend.tuto.demo.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existByName(Role role) {
        return this.roleRepository.existsByName(role.getName());
    }

    public Role create(Role role) {
        if (role.getPermissions() != null) {
            List<Long> resPermissions = role.getPermissions().stream().map(p -> p.getId()).toList();
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(resPermissions);
            role.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(role);
    }

    public Role fetchById(Long id) {
        Optional<Role> role = this.roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        }
        return null;
    }

    public Role update(Role role) {
        Role existingRole = this.roleRepository.findById(role.getId()).orElse(null);
        if (role.getPermissions() != null) {
            List<Long> resPermissions = role.getPermissions().stream().map(p -> p.getId()).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(resPermissions);
            role.setPermissions(dbPermissions);
        }
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole.setActive(role.isActive());
        existingRole.setPermissions(role.getPermissions());
        return this.roleRepository.save(existingRole);

    }

    public void delete(Long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> roles = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(roles.getTotalPages());
        meta.setTotal(roles.getTotalElements());
        res.setMeta(meta);
        res.setResult(roles.getContent());
        return res;

    }

}
