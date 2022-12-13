package com.eCommerce.demo.controllers;

import com.eCommerce.demo.models.dao.ProductDao;
import com.eCommerce.demo.services.ProductsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Iterator;

@Controller
@RequestMapping(path = "/")
public class TemplatesController {
    @Autowired
    private ProductsServices productsServices;


    @GetMapping(path = "login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping(path = "home")
    public String getHomeView() {
        return "home";
    }


    @GetMapping(path = "products")
    public ResponseEntity<ArrayList<ProductDao>> getProducts() {
        ArrayList<ProductDao> products = (ArrayList<ProductDao>) productsServices.getAllProducts().getResponse();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
