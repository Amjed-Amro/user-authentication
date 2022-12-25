package com.eCommerce.demo.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ResetPasswordDto {

    private final String password;
    private final String confirmPassword;
}
