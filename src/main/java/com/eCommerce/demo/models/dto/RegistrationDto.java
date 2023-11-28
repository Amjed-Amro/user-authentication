package com.eCommerce.demo.models.dto;

import com.eCommerce.demo.intities.AppUserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegistrationDto {

    private final String FIRST_NAME;
    private final String LAST_NAME;
    private final String EMAIL;
    private final String GENDER;
    private final Integer AGE;
    private final String USER_NAME;
    private final String PASSWORD;
    private final String CONFIRM_PASSWORD;
    private Set<AppUserRoles> roles;
    private String ipAddress;
    private Integer port;

}
