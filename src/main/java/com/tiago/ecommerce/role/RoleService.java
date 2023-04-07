package com.tiago.ecommerce.role;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    private final String ROLE_NOT_FOUND = "Role not found";

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public ResponseEntity<Object> delete(UUID id) {
        Role role = roleRepository.findById(id).orElse(null);

        if(role == null) {
            return ResponseEntity.status(404).body(ROLE_NOT_FOUND);
        }

        roleRepository.delete(role);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getById(UUID id) {
        Role role = roleRepository.findById(id).orElse(null);

        if(role == null) {
            return ResponseEntity.status(404).body(ROLE_NOT_FOUND);
        }

        return ResponseEntity.ok(role);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public ResponseEntity<Object> update(UUID id, RoleDto updatedRoleDto) {
        Role role = roleRepository.findById(id).orElse(null);

        if(role == null) {
            return ResponseEntity.status(404).body(ROLE_NOT_FOUND);
        }

        BeanUtils.copyProperties(updatedRoleDto, role);

        return ResponseEntity.ok(roleRepository.save(role));
    }
}
