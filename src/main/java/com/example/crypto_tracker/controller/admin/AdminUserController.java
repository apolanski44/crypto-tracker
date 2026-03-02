package com.example.crypto_tracker.controller.admin;

import com.example.crypto_tracker.dto.user.CreateUserDTO;
import com.example.crypto_tracker.dto.user.UpdateUserDTO;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid CreateUserDTO userDetails) {
        return userService.createUser(userDetails);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
