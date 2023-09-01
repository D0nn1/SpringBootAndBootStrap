package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.GrantedAuthority;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class SpringBootSecurityDemoApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        userService.addRoleToCurrentUser(1, "ROLE_ADMIN");


                List<User> users = userService.getAllUsers();
        users.forEach(u -> {
            System.out.print("User " + u.getName() + " with roles: ");
            u.getRoles().forEach(role ->
                    System.out.print(role.getAuthority() + " "));
            System.out.println();
        });

    }
}
