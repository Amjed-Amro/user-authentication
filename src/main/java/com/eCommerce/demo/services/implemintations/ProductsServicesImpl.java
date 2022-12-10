package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.intities.Product;
import com.eCommerce.demo.models.dao.ProductDao;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.repository.ProductsRepository;
import com.eCommerce.demo.services.ProductsServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import net.minidev.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.eCommerce.demo.constants.Constants.*;
import static com.eCommerce.demo.constants.Constants.RESPONSE_MESSAGE.*;
import static org.springframework.http.HttpMethod.*;
@Service
@Log4j2

public class ProductsServicesImpl implements ProductsServices {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductsRepository productsRepository;

    /**
     * this function can be used to save a list of products to database
     * @param productList is the list of product to be saved to database
     */
    @Override
    public void saveProducts(List<Product> productList) {
        try {
            productList.stream().forEach(product ->{
                productsRepository.save(product);
                log.info("successfully added one product to database");
            });
        }catch (Exception exception){
            log.error("error adding a product to database ". concat(exception.getMessage()));
        }
    }


    /**
     * this function is used to add a single product to database
     * @param product is the product to be saved to database
     */
    @Override
    public void saveProduct (Product product){
        try {
            productsRepository.save(product);
            log.info("successfully added one product to database");
        }catch (Exception exception){
            log.error("error adding a product to database ". concat(exception.getMessage()));
        }
    }

    /**
     * this method is used to get all the products from the database
     * and convert them to product data access object
     * @return a list of ProductDao instance that contains all
     * the required information form the database
     */
    @Override
    public ResponseDto getAllProducts (){
        try{
            List<Product> productList = productsRepository.findAll();
            List<ProductDao> productDaos =new ArrayList<>();
            log.info ("successfully loaded all products in database");
            productList.stream().forEach(product -> {
                productDaos.add(convertToDao(product));
            });
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, productDaos);
        }catch (Exception exception){
            log.error("error loading all products from database ".concat(exception.getMessage()));
            return new ResponseDto(FAILED, FAILED, exception.getMessage());
        }
    }

    /**
     * this method is to find all products with rating equal or
     * greater than the parameter and convert it to a
     * product data access object
     * @param rating is the rating to find the products with
     * @return return a list of ProductDao instance that contain
     * all the required information from the database
     */
    @Override
    public ResponseDto findAllByRatingGreaterThanEqual (Double rating){
        try{
            List<Product> productList = productsRepository.findAllByRatingGreaterThanEqual(rating);
            List<ProductDao> productDaos =new ArrayList<>();
            log.info (String.format("successfully loaded all products in database grater than or equal %s",rating));
            productList.stream().forEach(product -> {
                productDaos.add(convertToDao(product));
            });
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, productDaos);
        }catch (Exception exception){
            log.error(String.format("error loading all products from the database grater than or equal %s ",rating)
                    .concat(exception.getMessage()));
            return new ResponseDto(FAILED, FAILED, exception.getMessage());
        }
    }

    /**
     *  this method is to find all products with brand equal
     *  to the parameter and convert it to a
     *  product data access object
     * @param brand is the brand to find the products with
     * @return return a list of ProductDao instance that contain
     * all the required information from the database
     */
    @Override
    public ResponseDto findAllByBrand(String brand){
        try{
            List<Product> productList = productsRepository.findAllByBrand(brand);
            List<ProductDao> productDaos =new ArrayList<>();
            log.info (String.format("successfully loaded all products in database with brand %s",brand));
            productList.stream().forEach(product -> {
                productDaos.add(convertToDao(product));
            });
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, productDaos);
        }catch (Exception exception){
            log.error(String.format("error loading all products from the database with brand %s ",brand)
                    .concat(exception.getMessage()));
            return new ResponseDto(FAILED, FAILED, exception.getMessage());
        }
    }

    /**
     *  this method is to find all products with category equal
     *  to the parameter and convert it to a
     *  product data access object
     * @param category is the category to find the products with
     * @return return a list of ProductDao instance that contain
     * all the required information from the database
     */

    @Override
    public ResponseDto findAllByCategory(String category){
        try{
            List<Product> productList = productsRepository.findAllByCategory(category);
            List<ProductDao> productDaos =new ArrayList<>();
            log.info (String.format("successfully loaded all products in database with category %s ", category));
            productList.stream().forEach(product -> {
                productDaos.add(convertToDao(product));
            });
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, productDaos);
        }catch (Exception exception){
            log.error(String.format("error loading all products from the database with category %s ",category));
            log.error(String.format("exception: %s",exception.getMessage()));
            return new ResponseDto(FAILED, FAILED, APPLICATION_CONTROLLER_ERROR);
        }
    }

    /**
     * this method is used to get all the product from the url parameter
     * and convert them to product objects
     * @param url is the path to get the products from
     * @return a list of the products from the path
     * @throws JsonProcessingException
     */
    @Override
    public ResponseDto parseProducts(String url) throws JsonProcessingException {
        try {
            ArrayList<Product> products = new ArrayList<>();
            ArrayList jsonObjectsList = (ArrayList) restTemplate.exchange(url, GET, null,
                    new ParameterizedTypeReference<JSONObject>() {}).getBody().get("products");

            for (int i=0;i< jsonObjectsList.size();i++){
                LinkedHashMap<String, Object> jsonProduct = (LinkedHashMap<String, Object>) jsonObjectsList.get(i);
                Product product = new Product();
                product.setId((Integer) jsonProduct.get("id"));
                product.setTitle((String) jsonProduct.get("title"));
                product.setDescription((String) jsonProduct.get("description"));
                product.setPrice((Integer) jsonProduct.get("price"));
                product.setDiscountPercentage((Double) jsonProduct.get("discountPercentage"));
                Object rating  = (jsonProduct.get("rating")).toString();
                product.setRating(Double.parseDouble((String.valueOf(rating))));
                product.setStock((Integer) jsonProduct.get("stock"));
                product.setBrand((String) jsonProduct.get("brand"));
                product.setCategory((String) jsonProduct.get("category"));
                product.setThumbnailUrl((String) jsonProduct.get("thumbnail"));
                product.setImagesUrl((List<String>) jsonProduct.get("images"));
                products.add(product);
            }
            log.info(String.format("successfully loaded product from %s",url));
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, products);
        }catch (Exception exception){
            log.error(String.format("exception: %s",exception.getMessage()));
            return new ResponseDto(FAILED, FAILED, APPLICATION_CONTROLLER_ERROR);
        }

    }

    /**
     * this method is to convert products from the database
     * to ProductDto instance
     * @param product is the product to be converted
     * @return return new instance of ProductDto
     */
    private ProductDao convertToDao (Product product){
        ProductDao productDao = new ProductDao();
        productDao.setId(product.getId());
        productDao.setTitle(product.getTitle());
        productDao.setDescription(product.getDescription());
        productDao.setPrice(product.getPrice());
        productDao.setDiscountPercentage(product.getDiscountPercentage());
        productDao.setRating(product.getRating());
        productDao.setStock(product.getStock());
        productDao.setBrand(product.getBrand());
        productDao.setCategory(product.getCategory());
        productDao.setThumbnailUrl(product.getThumbnailUrl());
        productDao.setImagesUrl(product.getImagesUrl());
        return productDao;
    }
}
