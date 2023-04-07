package com.tiago.ecommerce.user;

import com.tiago.ecommerce.role.Role;
import com.tiago.ecommerce.role.RoleRepository;
import com.tiago.ecommerce.utils.EncodingUtils;
import com.tiago.ecommerce.utils.PrincipalUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final String USER_NOT_FOUND = "User not found";

    public ResponseEntity<Object> save(User user) throws RoleNotFoundException {
        if(PrincipalUtils.isUser()){
            return ResponseEntity.status(403).body("You don't have permission to create users");
        }

        Role role_user = roleRepository.findByNameIgnoreCase("role_user")
                                        .orElseThrow(RoleNotFoundException::new);

        user.getRoles().add(role_user);
        role_user.getUsers().add(user);

        userRepository.save(user);
        roleRepository.save(role_user);

        user.setPassword(EncodingUtils.encode(user.getPassword()));

        return PrincipalUtils.isAdmin()
                ? ResponseEntity.ok(user)
                : ResponseEntity.ok(userToDto(user));
    }

    public ResponseEntity<Object> delete(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        if(transactionalActionNotAllowed(username)) {
            return ResponseEntity.status(403).body("You don't have permission to delete this user");
        }

        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getById(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        return PrincipalUtils.isAdmin()
                ? ResponseEntity.ok(user)
                : ResponseEntity.ok(userToDto(user));
    }

    public List<?> getAll() {
        return PrincipalUtils.isAdmin()
                ? userRepository.findAll()
                : userRepository.findAll().stream().map(this::userToDto).toList();
    }

    public ResponseEntity<Object> update(String username, UserDto updatedUserDto) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElse(null);

        if(user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        if(transactionalActionNotAllowed(username)) {
            return ResponseEntity.status(403).body("You don't have permission to update this user");
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

    private UserDto userToDto(User user) {
        return new UserDto(user.getUsername(), null);
    }

    private boolean transactionalActionNotAllowed(String username) {
        return !username.equals(PrincipalUtils.getName()) && !PrincipalUtils.isAdmin();
    }
}