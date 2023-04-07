package com.tiago.ecommerce.role;

import com.tiago.ecommerce.utils.PrincipalUtils;
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

        return PrincipalUtils.isAdmin()
                ? ResponseEntity.ok(role)
                : ResponseEntity.ok(RoleToDto(role));
    }

    public List<?> getAll() {
        return PrincipalUtils.isAdmin()
                ? roleRepository.findAll()
                : roleRepository.findAll().stream().map(this::RoleToDto).toList();
    }

    public ResponseEntity<Object> update(UUID id, RoleDto updatedRoleDto) {
        Role role = roleRepository.findById(id).orElse(null);

        if(role == null) {
            return ResponseEntity.status(404).body(ROLE_NOT_FOUND);
        }

        BeanUtils.copyProperties(updatedRoleDto, role);

        return ResponseEntity.ok(roleRepository.save(role));
    }

    private RoleDto RoleToDto(Role role) {
        return new RoleDto(role.getAuthority());
    }
}
