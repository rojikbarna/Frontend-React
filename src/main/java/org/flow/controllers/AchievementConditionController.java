package org.flow.controllers;

import org.flow.configuration.Validations;
import org.flow.models.Achievement;
import org.flow.models.AchievementCondition;
import org.flow.repositories.AchievementConditionRepository;
import org.flow.repositories.AchievementRepository;
import org.flow.repositories.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path="/achievements")
public class AchievementConditionController {

    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private AchievementConditionRepository achievementConditionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    Validations validations = new Validations();

    //get achievement conditions
    @GetMapping(path = "/{id}/achievement_conditions")
    public @ResponseBody ResponseEntity findAchievementConditions(@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Iterable<AchievementCondition> allAchievementConditions = achievementConditionRepository.findAll();
                List<AchievementCondition> achievementConditionList = new ArrayList();
                for (AchievementCondition achievementCondition : allAchievementConditions) {
                    if (achievementCondition.getAchievement().getId().equals(achievementRepository.findById(id).get().getId())) {
                        achievementConditionList.add(achievementCondition);
                    }
                }
                Iterable<AchievementCondition> conditions = achievementConditionList;
                return ResponseEntity.ok(conditions);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //get achievement condition by ID
    @GetMapping(path = "/{id}/achievement_conditions/{id2}")
    public @ResponseBody ResponseEntity getAchievementConditionById(@PathVariable("id") Long id2, @RequestHeader(value = "Authorization") String token) throws AchievementNotFoundException {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Optional<AchievementCondition> achievementCondition = achievementConditionRepository.findById(id2);
                return ResponseEntity.ok(achievementCondition);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //create new achievement condition
    @PostMapping(path="/{id}/achievement_conditions")
    public @ResponseBody ResponseEntity addNewAchievementCondition (@PathVariable("id") Long id, @RequestBody String condition, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                AchievementCondition newAchievementCondition = new AchievementCondition();
                JSONObject jsonObject = new JSONObject(condition);
                newAchievementCondition.setQuantity(jsonObject.getInt("quantity"));
                newAchievementCondition.setAchievement(achievementRepository.findById(id).get());
                newAchievementCondition.setProduct(productRepository.findByName(jsonObject.getString("productName")));
                achievementConditionRepository.save(newAchievementCondition);
                return ResponseEntity.ok(newAchievementCondition);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //update achievement condition
    @PutMapping(path="/{id}/achievement_conditions/{id2}")
    public @ResponseBody ResponseEntity updateAchievementCondition(@PathVariable("id2") Long id,
                                                                         @RequestBody String condition, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                AchievementCondition achievementCondition = achievementConditionRepository.findById(id).get();
                JSONObject jsonObject = new JSONObject(condition);
                achievementCondition.setQuantity(jsonObject.getInt("quantity"));
                achievementCondition.setProduct(productRepository.findByName(jsonObject.getString("productName")));
                achievementConditionRepository.save(achievementCondition);
                return ResponseEntity.ok(achievementCondition);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //delete achievement condition
    @DeleteMapping(path="/{id}/achievement_conditions/{id2}")
    public @ResponseBody ResponseEntity deleteAchievementCondition(@PathVariable("id2") Long id2, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                achievementConditionRepository.deleteById(id2);
                return ResponseEntity.ok(achievementConditionRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }
}