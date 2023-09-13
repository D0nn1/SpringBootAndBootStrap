package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveUser(User user, List<String> roles) {
        Set<Role> listOfRoles = new HashSet<>();
        if (roles != null) {
            listOfRoles = convertStringToRole(roles);
        } else {
            listOfRoles.add(new Role("ROLE_USER"));
        }
        user.setRoles(listOfRoles);
        saveUser(user);
    }

    @Override
    public User getUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);

    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, List<String> roles) {
        Set<Role> listOfRoles = new HashSet<>();
        if (roles != null) {
            listOfRoles = convertStringToRole(roles);
        } else {
            listOfRoles.add(new Role("ROLE_USER"));
        }
        user.setRoles(listOfRoles);
        saveUser(user);
    }


    @Override
    public void removeRole(String roleName) {
        Role role = roleRepository.getRoleByAuthority(roleName);
        roleRepository.delete(role);
    }

    @Override
    public void removeRole(int userId, String roleName) {
        User user = userRepository.getById(userId);
        List<Role> userRoles = (List<Role>) user.getRoles();
        Role roleToRemove = userRoles.stream()
                .filter(role -> role.getAuthority().equals(roleName))
                .findFirst()
                .orElse(null);

        if (roleToRemove != null) {
            userRoles.remove(roleToRemove);
            user.setRoles(userRoles);
            userRepository.save(user);
        } else {
            System.out.printf("UserService.removeRole: Role authority=%s not found in %s",
                    roleName, user.getUsername());
        }
    }

    @Override
    @Transactional
    public void addRoleToCurrentUser(int userId, String roleName) {
        User user = userRepository.findByUsername("donni");
        Role newRole = roleRepository.getRoleByAuthority(roleName);
        if (newRole == null) {
            newRole = new Role(roleName);
            roleRepository.save(newRole);
        }
        Collection<Role> userRoles = user.getRoles();
        userRoles.add(newRole);
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    @Override
    public Set<Role> convertStringToRole(List<String> stringList) {
        Set<Role> listOfRoles = new HashSet<>();
        stringList.forEach(r -> listOfRoles.add(convertStringToRole(r)));
        return listOfRoles;
    }

    @Override
    public Role convertStringToRole(String string) {
        Role role = roleRepository.getRoleByAuthority(string);
        if (role == null) {
            role = new Role(string);
            roleRepository.save(role);
        }
        return role;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(String.format("User with name '%s' not found", username));
        return user;
    }


}
