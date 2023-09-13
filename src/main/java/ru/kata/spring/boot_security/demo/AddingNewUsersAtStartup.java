package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AddingNewUsersAtStartup implements CommandLineRunner {
    private UserService userService;

    @Autowired
    public AddingNewUsersAtStartup(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        final String roleAdmin = "ROLE_ADMIN";
        final String roleUser = "ROLE_USER";

        List<String> firstUserRoles = new ArrayList<>() {{
            add(roleUser);
            add(roleAdmin);
        }};
        List<String> secondUserRoles = new ArrayList<>() {{
            add(roleUser);
        }};
        List<String> thirdUserRoles = new ArrayList<>() {{
            add(roleUser);
            add(roleAdmin);
        }};

        User donni = new User();
        donni.setUsername("donni");
        donni.setName("Roman");
        donni.setSurname("Tynchirov");
        donni.setAge(20);
        donni.setPasswordAndEncode("100");

        User marlovo = new User();
        marlovo.setUsername("marlovo");
        marlovo.setName("Юрий");
        marlovo.setSurname("Катков");
        marlovo.setAge(0);
        marlovo.setPasswordAndEncode("100");

        User defaultUser = new User();
        defaultUser.setUsername("user");
        defaultUser.setName("Default");
        defaultUser.setSurname("User");
        defaultUser.setAge(45);
        defaultUser.setPasswordAndEncode("100");


        userService.saveUser(donni, firstUserRoles);
        userService.saveUser(marlovo, thirdUserRoles);
        userService.saveUser(defaultUser, secondUserRoles);


    }
}
