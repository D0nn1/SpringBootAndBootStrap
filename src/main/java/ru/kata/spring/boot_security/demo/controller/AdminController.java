package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/info")
    public String showAllUsers(@ModelAttribute("user") User user,
                               Authentication authentication, Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("newRole", "");
        model.addAttribute("allRoles", userService.getAllRoles());

        String username = authentication.getName();
        List<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .collect(Collectors.toList());

        model.addAttribute("username", username);
        model.addAttribute("navBarRoles", roles);

        User lonelyUser = (User) userService.loadUserByUsername(username);
        model.addAttribute("lonelyUser", lonelyUser);
        return "all-users";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(userService.setAndEncodePassword(user, passwordEncoder));
        return "redirect:/admin/info";
    }


    @PatchMapping("/saveUpdatedUser/{id}")
    public String saveUpdatedUser(@PathVariable int id,
                                  @ModelAttribute("updatedUser") User user) {
        user.setId(id);
        user.setRoles(userService.convertStringToRole(user
                .getRoles()
                .stream()
                .map(Role::getAuthority)
                .toList()));
        userService.saveUser(userService.setAndEncodePassword(user, passwordEncoder));
        return "redirect:/admin/info";
    }


    @PostMapping("/add-role")
    public String addRole(@RequestParam("newRole") String newRole) {
        userService.convertStringToRole(newRole);
        return "redirect:/admin/info";
    }


    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/info";
    }
}