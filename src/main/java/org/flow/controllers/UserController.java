package org.flow.controllers;


import org.flow.configuration.Validations;
import org.flow.models.User;
import org.flow.repositories.AchievementRepository;
import org.flow.repositories.SessionRepository;
import org.flow.repositories.UserAchievementRepository;
import org.flow.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    Validations validations = new Validations();

    //get all users
    @GetMapping
    public @ResponseBody
    ResponseEntity getAllUsers(@RequestHeader(value = "Authorization") String token) {
        if (validations.stayingALive(token)) {
            return ResponseEntity.ok(userRepository.findAllByOrderByLastNameAsc());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //get user by ID
    @GetMapping(path = "/{id}")
    public @ResponseBody
    ResponseEntity getUserById(@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) throws UserNotFoundException {
        if (validations.stayingALive(token)) {
            if (validations.checkUser(id, token)) {
                Optional<User> user = userRepository.findById(id);
                if (!user.isPresent()) {
                    //throw new UserNotFoundException("User not found.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
                }
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }


    //create new user
    @PostMapping
    public @ResponseBody
    ResponseEntity addNewUser(@RequestBody String user) {
        User newUser = new User();
        JSONObject jsonObject = new JSONObject(user);
        if (userRepository.findByEmail(jsonObject.getString("email")) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This e-mail address is already taken.");
        } else {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(jsonObject.getString("password"));
            newUser.setPassword(hashedPassword);
            newUser.setFirstName(jsonObject.getString("firstName"));
            newUser.setLastName(jsonObject.getString("lastName"));
            newUser.setNickName(jsonObject.getString("nickName"));
            newUser.setEmail(jsonObject.getString("email"));
            newUser.setRoleType(jsonObject.getString("role"));
            if (jsonObject.getInt("xp") < 0) {
                newUser.setXp(jsonObject.getInt("xp"));
            }
            Date dob = null;
            try {
                dob = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("dob"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newUser.setDob(dob);
            newUser.setGender(jsonObject.getBoolean("gender"));
            userRepository.save(newUser);
            return ResponseEntity.ok(newUser);
        }
    }

    //update user
    @PutMapping(path="/{id}")
    public @ResponseBody ResponseEntity updateUser (@PathVariable("id") Long id, @RequestBody String user, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if(validations.checkUser(id, token)) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                JSONObject jsonObject = new JSONObject(user);
                User updatedUser = userRepository.findById(id).get();
                if(passwordEncoder.matches(jsonObject.getString("oldPassword"), updatedUser.getPassword())) {
                    if(!jsonObject.getString("newPassword").equals("") && jsonObject.getString("newPassword") != null) {
                        String hashedPassword = passwordEncoder.encode(jsonObject.getString("newPassword"));
                        updatedUser.setPassword(hashedPassword);
                    }
                    updatedUser.setFirstName(jsonObject.getString("firstName"));
                    updatedUser.setLastName(jsonObject.getString("lastName"));
                    updatedUser.setNickName(jsonObject.getString("nickName"));
                    updatedUser.setEmail(jsonObject.getString("email"));
                    Date dob = null;
                    try {
                        dob = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("dob"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    updatedUser.setDob(dob);
                    updatedUser.setGender(jsonObject.getBoolean("gender"));
                    userRepository.save(updatedUser);
                    return ResponseEntity.ok(updatedUser);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //delete user by ID
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity deleteUser (@PathVariable("id") Long id, @RequestHeader(value = "Authorization")  String token) {
        if(validations.stayingALive(token)) {
            if (validations.checkUser(id, token)) {
                userRepository.deleteById(id);
                return ResponseEntity.ok(userRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }
}
