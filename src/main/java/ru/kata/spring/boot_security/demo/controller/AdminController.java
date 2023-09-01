package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
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
        model.addAttribute("newRole", "");
        model.addAttribute("allRoles", userService.getAllRoles());
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
                           @RequestParam(name = "selectedRoles", required = false) List<String> selectedRoles) {
        user.setPasswordAndEncode(user.getPassword());
        userService.saveUser(user, selectedRoles);
        return "redirect:/admin/info";
    }


    @PostMapping("/saveUpdatedUser")
    public String saveUpdatedUser(@ModelAttribute("updatedUser") User user,
                                  @RequestParam(name = "selectedRoles", required = false) List<String> selectedRoles) {
        userService.updateUser(user, selectedRoles);
        return "redirect:/admin/info";
    }

    @GetMapping("/updateInfo")
    public String updateInfo(@RequestParam("userId") int id, Model model) {
        model.addAttribute("updatedUser", userService.getUser(id));
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("selectedRoles", new ArrayList<Role>());
        return "user-update";
    }


    @PostMapping("/add-role")
    public String addRole(@RequestParam("newRole") String newRole) {
        userService.convertStringToRole(newRole);
        return "redirect:/admin/info";
    }

    @PostMapping("/remove-role")
    public String removeRole(@RequestParam("roleName") String roleName) {
        userService.removeRole(roleName);
        return "redirect:/admin/info";
    }



    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") int id) {
        userService.deleteUser(id);
        return "redirect:/admin/info";
    }
}

