package com.example.crypto_tracker.service.user;

import com.example.crypto_tracker.dto.user.CreateUserDTO;
import com.example.crypto_tracker.dto.user.UpdateUserDTO;
import com.example.crypto_tracker.exception.EmailAlreadyTakenException;
import com.example.crypto_tracker.exception.UserNotFoundException;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserUpdater userUpdater;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
       return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id:" + id + "not found"));
    }

    @Transactional
    public User createUser(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyTakenException("Email " + dto.getEmail() + " is already taken");
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, UpdateUserDTO dto) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userUpdater.updateEntityFromDto(dto, userToUpdate);

        return userRepository.save(userToUpdate);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(userToDelete);
    }
}
