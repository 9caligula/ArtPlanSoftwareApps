package com.ArtPlanSoftware.controller;

import com.ArtPlanSoftware.entity.UserEntity;
import com.ArtPlanSoftware.exception.UserAlreadyExistsException;
import com.ArtPlanSoftware.response.ResponseError;
import com.ArtPlanSoftware.response.ResponseSuccess;
import com.ArtPlanSoftware.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserEntity user) {
        try {
            userService.existsByUsername(user.getUsername()); // ex
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            ResponseError response = new ResponseError(e.getMessage(), e.getHttpStatus());
            return new ResponseEntity<>(response, e.getHttpStatus());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody UserEntity user) {
        try {
            userService.existsByUsername(user.getUsername());
            return new ResponseEntity<>(new ResponseSuccess(String.format("Success validate. User with username '%s' not found", user.getUsername())),
                    HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            ResponseError response = new ResponseError(e.getMessage(), e.getHttpStatus());
            return new ResponseEntity<>(response, e.getHttpStatus());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserEntity>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }
}
