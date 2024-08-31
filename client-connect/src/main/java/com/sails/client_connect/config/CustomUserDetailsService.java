package com.sails.client_connect.config;

import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.PasswordExpiredException;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final long PASSWORD_EXPIRY_DAYS = 90;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        checkPasswordExpiry(user);
        return new CustomUserDetails(user);

    }
    private void checkPasswordExpiry(User user) {
        LocalDateTime passwordLastSet = user.getPasswordLastSet();
        if (passwordLastSet != null) {
            LocalDateTime expiryDate = passwordLastSet.plusDays(PASSWORD_EXPIRY_DAYS);
            if (LocalDateTime.now().isAfter(expiryDate)) {
                throw new PasswordExpiredException("Password expired, please change your password.");
            }
        }
    }



}
