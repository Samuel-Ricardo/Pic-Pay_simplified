package com.picpay.payment.application.controllers;

import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController  {

    @Autowired
    private UserService service;

//    @PostMapping
//    public ResponseEntity<User> create(@RequestBody @Valid UserDTO DTO) {
//        return new ResponseEntity<>(service.saveUser(DTO), CREATED);
//    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAllUsers());
    }
}
