package com.eCommerce.demo.repository;

import com.eCommerce.demo.intities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends MongoRepository<Product, Integer> {

    public List<Product> findAllByBrand(String brand);

    public List<Product> findAllByRatingGreaterThanEqual(Double rating);

    public List<Product> findAllByCategory(String category);
}
