package com.eCommerce.demo.models.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserInfoDao {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String gender;
    private Integer age;

}
