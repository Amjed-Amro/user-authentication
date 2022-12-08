package com.eCommerce.demo.intities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private Integer id;
    private String title;
    private String description;
    private Integer price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String brand;
    private String category;
    private String thumbnailUrl;
    private List<String> imagesUrl;

    private String createdBy;
    private Date createdDate;
    private String createdIpAddress;
    private String updatedBy;
    private Date updatedDate;
    private String updatedIpAddress;


}
