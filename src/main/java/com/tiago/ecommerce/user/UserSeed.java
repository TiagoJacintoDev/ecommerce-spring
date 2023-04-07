package com.tiago.ecommerce.user;

import com.tiago.ecommerce.role.Role;
import com.tiago.ecommerce.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class UserSeed implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() throws RoleNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase("user").orElse(null);
        User admin = userRepository.findByUsernameIgnoreCase("admin").orElse(null);

        if(user == null) {
            Role user_role = roleRepository.findByNameIgnoreCase("role_user")
                    .orElseThrow(RoleNotFoundException::new);

            User newUser = new User();
            newUser.setUsername("user");
            newUser.setPassword("user");

            newUser.getRoles().add(user_role);
            user_role.getUsers().add(newUser);

            userRepository.save(newUser);
        }

        if(admin == null) {
            Role admin_role = roleRepository.findByNameIgnoreCase("role_admin")
                    .orElseThrow(RoleNotFoundException::new);

            User newAdmin = new User();
            newAdmin.setUsername("admin");
            newAdmin.setPassword("admin");

            newAdmin.getRoles().add(admin_role);
            admin_role.getUsers().add(newAdmin);

            userRepository.save(newAdmin);
        }
    }
}
