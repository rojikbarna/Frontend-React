package org.flow.controllers;

import org.flow.configuration.Validations;
import org.flow.models.Achievement;
import org.flow.models.UserAchievement;
import org.flow.repositories.AchievementRepository;
import org.flow.repositories.SessionRepository;
import org.flow.repositories.UserAchievementRepository;
import org.flow.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/achievements")
public class AchievementController {

    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    Validations validations = new Validations();

    //get all achievements
    @GetMapping
    public @ResponseBody ResponseEntity getAllAchievements(@RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            return ResponseEntity.ok(achievementRepository.findAllByOrderByExpirationAsc());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //get all active achievements
    @GetMapping(path = "/active")
    public @ResponseBody ResponseEntity getAllActiveAchievements(@RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            long id = sessionRepository.findByToken(token).getUser().getId();
            Iterable<UserAchievement> allUserAchievements = userAchievementRepository.findAll();
            List<UserAchievement> userAchievements = new ArrayList<>();
            List<Achievement> ownAchievements = new ArrayList<>();
            for (UserAchievement userAchievement : allUserAchievements) {
                if (userAchievement.getUser().getId().equals(userRepository.findById(id).get().getId())) {
                    userAchievements.add(userAchievement);
                }
            }
            for (UserAchievement userAchievement : userAchievements) {
                Optional<Achievement> achievement = achievementRepository.findById(userAchievement.getAchievement().getId());
                ownAchievements.add(achievement.get());

            }
            Date now = new Date();
            Iterable<Achievement> allAchievements = achievementRepository.findAllByOrderByExpirationAsc();
            Iterable<Achievement> expiredAchievements = achievementRepository.findByExpirationBefore(now);
            List<Achievement> allActiveAchievementsList = new ArrayList<>();
            List<Achievement> allExpiredAchievementsList = new ArrayList<>();
            for (Achievement achievement : allAchievements) {
                allActiveAchievementsList.add(achievement);
            }
            for (Achievement achievement : expiredAchievements) {
                allExpiredAchievementsList.add(achievement);
            }
            allActiveAchievementsList.removeAll(allExpiredAchievementsList);
            allActiveAchievementsList.removeAll(ownAchievements);
            return ResponseEntity.ok(allActiveAchievementsList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //get achievement by ID
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity getAchievementById (@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token)  throws AchievementNotFoundException {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Optional<Achievement> achievement = achievementRepository.findById(id);
                if (!achievement.isPresent()) {
                    //throw new AchievementNotFoundException("Achievement not found.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

                }
                return ResponseEntity.ok(achievement);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //create new achievement
    @PostMapping
    public @ResponseBody ResponseEntity addNewAchievement (@RequestBody String achievement, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                JSONObject jsonObject = new JSONObject(achievement);
                Achievement newAchievement = new Achievement();
                newAchievement.setName(jsonObject.getString("name"));
                newAchievement.setDescription(jsonObject.getString("description"));
                newAchievement.setXpValue(jsonObject.getInt("xpValue"));
                Date expiration = null;
                try {
                    expiration = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("expiration"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newAchievement.setExpiration(expiration);
                achievementRepository.save(newAchievement);
                return ResponseEntity.ok(newAchievement);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //update achievement
    @PutMapping(path="/{id}")
    public @ResponseBody ResponseEntity updateAchievement(@PathVariable("id") Long id, @RequestBody String achievement, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Achievement updatedAchievement = achievementRepository.findById(id).get();
                JSONObject jsonObject = new JSONObject(achievement);
                updatedAchievement.setName(jsonObject.getString("name"));
                updatedAchievement.setDescription(jsonObject.getString("description"));
                updatedAchievement.setXpValue(jsonObject.getInt("xpValue"));
                Date expiration = null;
                try {
                    expiration = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("expiration"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                updatedAchievement.setExpiration(expiration);
                achievementRepository.save(updatedAchievement);
                return ResponseEntity.ok(updatedAchievement);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //delete achievement by ID
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity deleteAchievement(@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        if (validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                achievementRepository.deleteById(id);
                return ResponseEntity.ok(achievementRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

}
