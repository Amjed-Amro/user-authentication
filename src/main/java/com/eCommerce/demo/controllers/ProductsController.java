package com.eCommerce.demo.controllers;

import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.ProductsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "products")
public class ProductsController {

    @Autowired
    private ProductsServices productsServices;


    @GetMapping (path = "loadAllProducts")
    public ResponseEntity<ResponseDto> loadAllProducts(){
        return new ResponseEntity<>(productsServices.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping (path = "loadAllByRatingGreaterThanEqual/{rating}")
    public ResponseEntity<ResponseDto> loadAllByRatingGreaterThanEqual(@PathVariable Double rating){
        return new ResponseEntity<>(productsServices.findAllByRatingGreaterThanEqual(rating), HttpStatus.OK);
    }
    @GetMapping (path = "loadAllByBrand/{brand}")
    public ResponseEntity<ResponseDto> loadAllByBrand(@PathVariable String brand){
        return new ResponseEntity<>(productsServices.findAllByBrand(brand), HttpStatus.OK);
    }
    @GetMapping (path = "loadAllByCategory/{category}")
    public ResponseEntity<ResponseDto> loadAllByCategory(@PathVariable String category){
        return new ResponseEntity<>(productsServices.findAllByCategory(category), HttpStatus.OK);
    }
}
