package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import demo.backend.tuto.demo.domain.Role;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.service.RoleService;
import demo.backend.tuto.demo.utils.annotation.ApiMessage;
import demo.backend.tuto.demo.utils.exception.IdInvalidException;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService=roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Role created")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        if (this.roleService.existByName(role)) {
            throw new IdInvalidException("Role with Id" + role.getName() + " already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Role updated")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        if (this.roleService.fetchById(role.getId()) == null) {
            throw new IdInvalidException("Role with Id" + role.getId() + " does not exist");
        }
        if (this.roleService.existByName(role)) {
            throw new IdInvalidException("Role with name" + role.getName() + " already exists");
        }
        return ResponseEntity.ok(this.roleService.update(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Role deleted")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) throws IdInvalidException {
        if (this.roleService.fetchById(id) == null) {
            throw new IdInvalidException("Role with Id" + id + " does not exist");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getRoles(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.fetchRoles(spec, pageable));
    }
}
