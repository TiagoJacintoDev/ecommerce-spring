package com.tiago.ecommerce.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RoleSeed implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        Role role_user = roleRepository.findByNameIgnoreCase("role_user").orElse(null);
        Role role_admin = roleRepository.findByNameIgnoreCase("role_admin").orElse(null);

        if(role_user == null) {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            roleRepository.save(newRole);
        }

        if(role_admin == null) {
            Role newRole = new Role();
            newRole.setName("ROLE_ADMIN");
            roleRepository.save(newRole);
        }
    }
}
