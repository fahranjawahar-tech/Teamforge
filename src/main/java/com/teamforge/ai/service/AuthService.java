package com.teamforge.ai.service;

import com.teamforge.ai.dto.LoginRequest;
import com.teamforge.ai.dto.LoginResponse;
import com.teamforge.ai.model.User;
import com.teamforge.ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse authenticate(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return LoginResponse.failure("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return LoginResponse.failure("Password is required");
        }

        String username = request.getUsername().trim();
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return LoginResponse.failure("Invalid username or password");
        }

        User user = userOpt.get();
        String hashedPassword = hashPassword(request.getPassword().trim());

        // Check against hashed password or plaintext fallback (for sample init)
        boolean passwordMatches = user.getPassword().equals(hashedPassword) || 
                                 user.getPassword().equals(request.getPassword().trim());

        if (!passwordMatches) {
            return LoginResponse.failure("Invalid username or password");
        }

        String token = UUID.randomUUID().toString();
        return LoginResponse.success(token, user.getUsername(), user.getFullName(), user.getRole(), user.getDepartment());
    }

    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return plainPassword; // Fallback
        }
    }
}
