package com.tiago.ecommerce.role;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public Iterable<?> getRoles() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable UUID id) {
        return roleService.getById(id);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public Role createRole(@RequestBody RoleDto roleDto) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        return roleService.save(role);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> updateRole(@PathVariable UUID id, @RequestBody RoleDto roleDto) {
        return roleService.update(id, roleDto);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> deleteRole(@PathVariable UUID id) {
        return roleService.delete(id);
    }
}
