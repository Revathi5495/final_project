package com.sails.client_connect.service;



import com.sails.client_connect.dto.UserAuth;
import com.sails.client_connect.dto.UserDto;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.repository.RoleRepository;
import com.sails.client_connect.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public void saveUser(UserAuth userAuth) throws MessagingException {


        if (userAuth.getRoleNames().contains(RoleName.ADMIN)) {


            if (userRepository.findByRoles_Name(RoleName.ADMIN).isPresent()) {
                throw new IllegalStateException("An admin already exists. Cannot add another admin.");
            }
        }

        String dummyPassword = userAuth.getPassword();
        String encodedPassword = passwordEncoder.encode(dummyPassword); //hashing the password
        String fromEmail = "dummy.rip69@gmail.com";


        User user = new User();
        user.setUsername(userAuth.getUsername());
        user.setEmail(userAuth.getEmail());
        user.setPassword(encodedPassword);


        Set<Role> roles = userAuth.getRoleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);


        userRepository.save(user);

        emailService.sendDynamicEmail(fromEmail,
                user.getEmail(),
                "Your Account Credentials",
                "Username: " + user.getUsername() + "\nPassword: " + dummyPassword);
    }

    public List<UserDto> findAllUsers() {

        return userRepository.findAllWithRoles().stream()
                .map(user -> new UserDto(
                        user.getUser_id(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }


    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    
}
