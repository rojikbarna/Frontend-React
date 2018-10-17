package org.flow.controllers;
import com.google.zxing.WriterException;
import org.flow.configuration.Validations;
import org.flow.models.OrderLine;
import org.flow.models.Ordering;
import org.flow.qr_code.QRCGenerator;
import org.flow.repositories.OrderLineRepository;
import org.flow.repositories.OrderingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/ordering")
public class OrderingController {

    @Autowired
    private OrderingRepository orderingRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    Validations validations = new Validations();
    @Autowired
    QRCGenerator qrcGenerator = new QRCGenerator();

    //get all orderings
    @GetMapping
    public @ResponseBody ResponseEntity findAllOrders (@RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                return ResponseEntity.ok(orderingRepository.findAllByOrderByCreatedDesc());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    // get ordering by ID
    @GetMapping(path="/{id}")
    public @ResponseBody ResponseEntity getProductById (@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Optional<Ordering> ordering = orderingRepository.findById(id);
                return ResponseEntity.ok(ordering);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //create new ordering
    @PostMapping
    public @ResponseBody ResponseEntity addNewOrdering (/*@RequestHeader(value = "Authorization") String token*/) {
        //if(validations.stayingALive(token)) {
          //  if (validations.isAdmin(token)) {
                Ordering newOrdering = new Ordering();
                newOrdering.setQrCodePath("qrCodePath");
                orderingRepository.save(newOrdering);
                return ResponseEntity.ok(newOrdering);
            /*} else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }*/
    }
 

    //delete ordering by ID
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity deleteOrdering (@PathVariable("id") Long id, @RequestHeader String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                orderingRepository.deleteById(id);
                return ResponseEntity.ok(orderingRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }
}
