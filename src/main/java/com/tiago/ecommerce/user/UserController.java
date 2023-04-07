package com.tiago.ecommerce.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable<?> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) throws RoleNotFoundException {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return userService.save(user);
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        return userService.delete(id);
    }

    @PutMapping("/{userId}/role/{roleId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> assignRoleToUser(@PathVariable UUID userId, @PathVariable UUID roleId) {
        return userService.assignRoleToUser(userId, roleId);
    }
}
