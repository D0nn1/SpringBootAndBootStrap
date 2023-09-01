package ru.kata.spring.boot_security.demo.service;



import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    List<Role> getAllRoles();

    void saveUser(User user);

    void saveUser(User user, List<String> roles);

    User getUser(int id);

    void deleteUser(int id);

    void updateUser(User user);

    void updateUser(User user, List<String> roles);

    void removeRole(int userId, String roleName);
    void removeRole(String roleName);

    void addRoleToCurrentUser(int userId, String roleName);

    Collection<Role> convertStringToRole(List<String> stringList);

    Role convertStringToRole(String string);
}
