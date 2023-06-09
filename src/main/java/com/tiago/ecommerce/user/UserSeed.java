package com.tiago.ecommerce.user;

import com.tiago.ecommerce.role.Role;
import com.tiago.ecommerce.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class UserSeed implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() throws RoleNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase("user").orElse(null);
        User admin = userRepository.findByUsernameIgnoreCase("admin").orElse(null);

        if(user == null) {
            Role role_user = roleRepository.findByNameIgnoreCase("role_user")
                    .orElseThrow(RoleNotFoundException::new);

            User newUser = new User();
            newUser.setUsername("user");
            newUser.setPassword(passwordEncoder.encode("user"));

            newUser.getRoles().add(role_user);
            role_user.getUsers().add(newUser);

            userRepository.save(newUser);
        }

        if(admin == null) {
            Role role_admin = roleRepository.findByNameIgnoreCase("role_admin")
                    .orElseThrow(RoleNotFoundException::new);

            User newAdmin = new User();
            newAdmin.setUsername("admin");
            newAdmin.setPassword(passwordEncoder.encode("admin"));

            newAdmin.getRoles().add(role_admin);
            role_admin.getUsers().add(newAdmin);

            userRepository.save(newAdmin);
        }
    }
}
