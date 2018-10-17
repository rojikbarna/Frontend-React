package org.flow.controllers;

import com.google.zxing.WriterException;
import org.flow.configuration.Validations;
import org.flow.models.OrderLine;
import org.flow.models.Ordering;
import org.flow.models.Product;
import org.flow.qr_code.QRCGenerator;
import org.flow.repositories.OrderLineRepository;
import org.flow.repositories.OrderingRepository;
import org.flow.repositories.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/ordering")
public class OrderLineController {

    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private OrderingRepository orderingRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    Validations validations = new Validations();

    //get all orderLines for the current ordering
    @GetMapping(path="/{id}/orderlines")
    public @ResponseBody ResponseEntity findOrderLines (@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Iterable<OrderLine> allOrderLines = orderLineRepository.findAll();
                List<OrderLine> orderLineList = new ArrayList<>();
                for (OrderLine orderLine : allOrderLines) {
                    if (orderLine.getOrdering().getId().equals(orderingRepository.findById(id).get().getId())) {
                        orderLineList.add(orderLine);
                    }
                }
                Iterable<OrderLine> lines = orderLineList;
                return ResponseEntity.ok(lines);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }


    //get orderLine by ID
    @GetMapping(path="/{id}/orderlines/{id2}")
    public @ResponseBody ResponseEntity getOrderLineById (@PathVariable("id") Long id, @PathVariable("id2") Long id2, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Optional<OrderLine> orderLine = orderLineRepository.findById(id2);
                return ResponseEntity.ok(orderLine);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //create new orderLine
    @PostMapping(path="/{id}/newOrder")
    public @ResponseBody ResponseEntity addNewOrderLine (@PathVariable("id") Long orderId, @RequestBody String orderLines /*@RequestHeader(value = "Authorization") String token*/) {
        //if(validations.stayingALive(token)) {
            //if (validations.isAdmin(token)) {
                System.out.println(orderLines);

                JSONObject jsonObject = new JSONObject(orderLines);
                System.out.println(jsonObject);
                String lines = jsonObject.toString();
                lines = lines.replace("{", "");
                lines = lines.replace("}", "");
                lines = lines.replace("\"", "");
                String [] lineArray = lines.split("[,:]");
                for(int i = 0; i < lineArray.length; i++) {
                    OrderLine newOrderLine = new OrderLine();
                    newOrderLine.setOrdering(orderingRepository.findById(orderId).get());
                    newOrderLine.setProduct(productRepository.findByName(lineArray[i]));
                    i++;
                    newOrderLine.setQuantity(Integer.parseInt(lineArray[i]));
                    orderLineRepository.save(newOrderLine);
                }
                QRCGenerator qrcGenerator = new QRCGenerator();
                String path = "";
                try {
                    path = qrcGenerator.generateQRCode(orderId);
                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                }
                Ordering generated = new Ordering();
                generated = orderingRepository.findById(orderId).get();
                System.out.println(generated.getId());
                generated.setQrCodePath(path);
                orderingRepository.save(generated);
                return ResponseEntity.ok(path);


            /*} else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }*/
    }

    //update orderLine
    @PutMapping(path="/{id}/orderlines/{id2}")
    public @ResponseBody ResponseEntity updateOrderLine (@PathVariable("id2") Long id, @RequestBody String orderLine, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                OrderLine updatedOrderLine = orderLineRepository.findById(id).get();
                JSONObject jsonObject = new JSONObject(orderLine);
                updatedOrderLine.setProduct(productRepository.findByName(jsonObject.getString("productName")));
                updatedOrderLine.setQuantity(jsonObject.getInt("quantity"));
                orderLineRepository.save(updatedOrderLine);
                return ResponseEntity.ok(updatedOrderLine);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }


    //delete orderLine by ID
    @DeleteMapping(path = "/{id}/orderlines/{id2}")
    public @ResponseBody ResponseEntity deleteOrderLine (@PathVariable("id") Long orderId, @PathVariable("id2") Long id, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                orderLineRepository.deleteById(id);
                return ResponseEntity.ok(orderLineRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }
}
