package com.example.expensetracker.controller;

import com.example.expensetracker.entity.User;
import com.example.expensetracker.exception.UserAlreadyExistsException;
import com.example.expensetracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<?> createUser(@RequestBody User user){
//        try{
//            User newUser = userService.createUser(user);
//            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//
//        } catch (UserAlreadyExistsException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        catch (IllegalArgumentException e){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username){
        Optional<User> user = userService.findUserByUsername(username);

        if(user.isEmpty()){
            return new ResponseEntity<>("Username Not Found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
