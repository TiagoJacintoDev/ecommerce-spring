package com.tiago.ecommerce.user;

import com.tiago.ecommerce.role.Role;
import com.tiago.ecommerce.role.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final String USER_NOT_FOUND = "User not found";

    public User save(User user) {
        return userRepository.save(user);
    }

    public ResponseEntity<Object> delete(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getById(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        return ResponseEntity.ok(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public ResponseEntity<Object> update(UUID id, UserDto updatedUserDto) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        BeanUtils.copyProperties(updatedUserDto, user);

        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<Object> assignRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId).orElse(null);
        Role role = roleRepository.findById(roleId).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        if(role == null) {
            return ResponseEntity.status(404).body("Role not found");
        }

        user.getRoles().add(role);
        role.getUsers().add(user);

        userRepository.save(user);
        roleRepository.save(role);

        return ResponseEntity.ok("Role assigned to user");
    }
}