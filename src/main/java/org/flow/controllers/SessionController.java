package org.flow.controllers;

import org.flow.models.Session;
import org.flow.models.User;
import org.flow.repositories.SessionRepository;
import org.flow.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping(path="/sessions")
public class SessionController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping
    public @ResponseBody ResponseEntity login(@RequestBody String login) {
        JSONObject jsonObject = new JSONObject(login);
        User loggedUser = userRepository.findByEmail(jsonObject.getString("email"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(jsonObject.getString("password"), loggedUser.getPassword())) {
            String token = UUID.randomUUID().toString();
            Session session = new Session();
            session.setToken(token);
            session.setUser(loggedUser);
            Date date = new Date();
            date.setTime(date.getTime() + 300000);
            session.setExpiration(date);
            sessionRepository.save(session);
            long userID = loggedUser.getId();
            String credentials = token + "." + String.valueOf(userID);
            System.out.println(credentials);
            return new ResponseEntity<>(credentials, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail and password do not match.");
        }
    }

    @PostMapping(path="/admin")
    public @ResponseBody ResponseEntity adminLogin(@RequestBody String login) {
        JSONObject jsonObject = new JSONObject(login);
        User loggedUser = userRepository.findByEmail(jsonObject.getString("email"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(jsonObject.getString("password"), loggedUser.getPassword()) &&
                String.valueOf(loggedUser.getRoleType()).equals("ADMIN")) {
            String token = UUID.randomUUID().toString();
            Session session = new Session();
            session.setToken(token);
            session.setUser(loggedUser);
            Date date = new Date();
            date.setTime(date.getTime() + 300000);
            session.setExpiration(date);
            sessionRepository.save(session);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("BAD", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(path="/{id}")
    public @ResponseBody ResponseEntity logout(@PathVariable("id") Long id, @RequestHeader(value = "Authorization")  String token) {
        sessionRepository.delete(sessionRepository.findByToken(token));
        return ResponseEntity.ok("LOGGED OUT");
    }

    @PostMapping(path="/badsession")
    public @ResponseBody ResponseEntity userLoginToAdminPage (@RequestHeader String badlogin) {
        System.out.println("try");
        JSONObject jsonObject = new JSONObject(badlogin);
        sessionRepository.delete(sessionRepository.findByToken(badlogin));
        return ResponseEntity.ok("You don't have permission to this site");
    }
}
