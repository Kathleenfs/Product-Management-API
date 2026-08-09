package io.github.kathleenfs.productmanagementapi.controller;

import io.github.kathleenfs.productmanagementapi.dto.request.LoginRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.request.UserRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.LoginResponseDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.UserResponseDTO;
import io.github.kathleenfs.productmanagementapi.service.AuthService;
import io.github.kathleenfs.productmanagementapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(
            @Valid @RequestBody UserRequestDTO request
    ) {
        return userService.create(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return authService.login(request);
    }
}