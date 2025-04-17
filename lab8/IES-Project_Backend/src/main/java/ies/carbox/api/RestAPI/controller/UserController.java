package ies.carbox.api.RestAPI.controller;

import ies.carbox.api.RestAPI.CONSTANTS;
import ies.carbox.api.RestAPI.dtos.LoginUserDto;
import ies.carbox.api.RestAPI.dtos.RegisterUserDto;
import ies.carbox.api.RestAPI.entity.User;
import ies.carbox.api.RestAPI.service.AuthenticationService;
import ies.carbox.api.RestAPI.service.JwtService;
import ies.carbox.api.RestAPI.service.UserService;
import ies.carbox.api.RestAPI.token.AuthToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UserController provides endpoints for managing user accounts, including registration, login,
 * updating account details, retrieving user information, and logout.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(CONSTANTS.apiBase + "/user")
@io.swagger.v3.oas.annotations.tags.Tag(name = "User Management", description = "Endpoints for user account operations")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Creates a new user account.
     *
     * <p>This endpoint is for user registration. It accepts a `User` object in the request body
     * and returns the created user object with a 201 status on success.</p>
     *
     * @param user The User object containing registration information.
     * @return ResponseEntity with the created User object and HTTP status 201, or 400 if creation fails.
     */
    @PostMapping("/accountCreation")
    @Operation(
        summary = "Register a new user account",
        description = "Create a new user account with the provided user details",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid user details or creation failed")
        }
    )
    public ResponseEntity<User> createAccount(
            @Parameter(description = "User object with account details")
            @Valid @RequestBody RegisterUserDto user
        ) {
        try {
            if (userService.loadUserByUsername(user.getEmail()) != null) {
                return ResponseEntity.status(409).body(null);
            }
            User createdUser = authenticationService.signup(user);
            return ResponseEntity.status(201).body(createdUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Authenticates a user and generates a token.
     *
     * @param loginRequest User object containing login credentials.
     * @return ResponseEntity with token (String) and HTTP status 200, or 401 if authentication fails.
     */
    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user and generate token",
        description = "Authenticate the user and generate an authentication token",
        responses = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
        }
    )
    public ResponseEntity<AuthToken> login(
        @Parameter(description = "User object with login credentials") @RequestBody LoginUserDto loginRequest
        ) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginRequest);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            AuthToken authToken = new AuthToken();
            authToken.setToken(jwtToken);
            authToken.setRole(authenticatedUser.getAuthorities().iterator().next().toString());
            authToken.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(authToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(null);
        }
    }

    /**
     * Updates user account information.
     *
     * @param updatedUser The User object containing updated information.
     * @return ResponseEntity with updated User object and HTTP status 200, or 400 if update fails.
     */
    @PutMapping("/account")
    @Operation(
        summary = "Update user account information",
        description = "Update the user account with the provided details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User account updated successfully",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid data or update failed")
        }
    )
    public ResponseEntity<User> updateAccount(
            @Parameter(description = "Updated User object with new details") @RequestBody User updatedUser) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            User updated = userService.updateUserDetails(currentUser.getEmail(), updatedUser);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    /**
     * Retrieves account details for a specific user.
     *
     * @param userId The ID of the user whose details are requested.
     * @return ResponseEntity with User object and HTTP status 200, or 404 if user is not found.
     */
    @GetMapping("/account")
    @Operation(
        summary = "Retrieve user account details by user ID",
        description = "Retrieve the details of a user account based on user ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User found and returned successfully",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    public ResponseEntity<User> getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String email = currentUser.getEmail();
        try {
            User user = userService.loadUserByUsername(email);
            System.out.println(user);
            return ResponseEntity.status(200).body(user);

        } catch (UsernameNotFoundException e) {
            System.out.println("INFO: User \"" + email + "\" not found");
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(500).body(null);
    }

    /**
     * Logs out the user.
     *
     * @return ResponseEntity with a success message and HTTP status 200.
     */
    @PostMapping("/logout")
    @Operation(
        summary = "Logout user",
        description = "Logs out the current user",
        responses = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully")
        }
    )
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}