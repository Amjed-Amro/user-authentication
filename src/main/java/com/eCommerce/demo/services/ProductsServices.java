package com.eCommerce.demo.services;

import com.eCommerce.demo.intities.Product;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ProductsServices {

    public ResponseDto parseProducts(String url) throws JsonProcessingException;
    public void saveProducts (List<Product> productList);
    public void saveProduct (Product product);
    public ResponseDto getAllProducts ();
    public ResponseDto findAllByRatingGreaterThanEqual (Double rating);
    public ResponseDto findAllByBrand(String brand);
    public ResponseDto findAllByCategory(String category);
}
