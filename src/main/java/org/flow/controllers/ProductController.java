package org.flow.controllers;

import org.flow.configuration.Validations;
import org.flow.models.Product;
import org.flow.repositories.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    Validations validations = new Validations();

    //get all products
    @GetMapping
    public @ResponseBody ResponseEntity findAllProducts (@RequestHeader(value = "Authorization") String token){
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                return ResponseEntity.ok(productRepository.findAllByOrderByCategoryAscNameAsc());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    // get product by ID
    @GetMapping(path="/{id}")
    public @ResponseBody ResponseEntity getProductById (@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                Optional<Product> product = productRepository.findById(id);
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

     //create new product
    @PostMapping
    public @ResponseBody ResponseEntity addNewProduct (@RequestBody String product, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                JSONObject jsonObject = new JSONObject(product);
                Product newProduct = new Product();
                newProduct.setName(jsonObject.getString("name"));
                newProduct.setCategory(Product.CategoryType.valueOf(jsonObject.getString("category")));
                newProduct.setPrice(jsonObject.getInt("price"));
                newProduct.setXpValue(jsonObject.getInt("xpValue"));
                productRepository.save(newProduct);
                return ResponseEntity.ok(newProduct);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    // update product
    @PutMapping(path="/{id}")
    public @ResponseBody ResponseEntity updateProduct (@PathVariable("id") Long id, @RequestBody String product,  @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                JSONObject jsonObject = new JSONObject(product);
                Product updatedProduct = productRepository.findById(id).get();
                updatedProduct.setName(jsonObject.getString("name"));
                updatedProduct.setCategory(Product.CategoryType.valueOf(jsonObject.getString("category")));
                updatedProduct.setPrice(jsonObject.getInt("price"));
                updatedProduct.setXpValue(jsonObject.getInt("xpValue"));
                productRepository.save(updatedProduct);
                return ResponseEntity.ok(updatedProduct);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }

    //delete product by ID
    @DeleteMapping(path="/{id}")
    public @ResponseBody ResponseEntity deleteProduct (@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        if(validations.stayingALive(token)) {
            if (validations.isAdmin(token)) {
                productRepository.deleteById(id);
                return ResponseEntity.ok(productRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You shall not pass.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session validations.");
        }
    }
}
