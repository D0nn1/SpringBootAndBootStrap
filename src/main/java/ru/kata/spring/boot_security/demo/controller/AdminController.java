package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "all-users";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("selectedRoles", new ArrayList<Role>());
        return "user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @ModelAttribute("selectedRoles") ArrayList<Role> selectedRoles) {
        user.setRoles(selectedRoles);
        System.out.println("Selected roles: ");
        userService.saveUser(user);
        return "redirect:/admin/info";
    }

    @PostMapping("/saveUpdatedUser")
    public String saveUpdatedUser(@ModelAttribute("updatedUser") User user) {
        userService.updateUser(user);
        return "redirect:/admin/info";
    }

    @GetMapping("/updateInfo")
    public String updateInfo(@RequestParam("userId") int id, Model model) {
        model.addAttribute("updatedUser", userService.getUser(id));
        return "user-update";
    }

    @PostMapping("/remove-role/{userId}")
    public String removeRole(@PathVariable("userId") int userId, @RequestParam("roleName") String roleName) {
        userService.removeRoleFromUser(userId, roleName);
        return "redirect:/admin/updateInfo?userId=" + userId;
    }

    @PostMapping("/add-role/{userId}")
    public String addRole(@PathVariable("userId") int userId, @RequestParam("newRole") String newRole) {
        userService.addRoleToCurrentUser(userId, newRole);
        return "redirect:/admin/updateInfo?userId=" + userId;
    }


    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") int id) {
        userService.deleteUser(id);
        return "redirect:/admin/info";
    }
}

