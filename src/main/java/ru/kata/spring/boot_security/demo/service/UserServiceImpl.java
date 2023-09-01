package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public User getUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElse(null);
        return user;
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
    public void removeRoleFromUser(int userId, String roleName) {
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
            System.out.printf("Role authority=%s not found in %s",
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
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(String.format("User with name '%s' not found", username));
        return user;
    }


}
