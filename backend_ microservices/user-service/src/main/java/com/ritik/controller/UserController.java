package com.ritik.controller;

import com.ritik.exception.UserException;
import com.ritik.modal.User;
import com.ritik.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){

        User createdUser = userService.createUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers(){

        List<User> users = userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserException {

        User user = userService.getUserById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("api/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                           @RequestBody User user) throws UserException {

        User updatedUser = userService.updateUser(id, user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) throws UserException {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User with id :: " + id + " deleted successfully", HttpStatus.ACCEPTED);
    }
}
