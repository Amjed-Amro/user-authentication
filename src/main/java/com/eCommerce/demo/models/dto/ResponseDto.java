package com.eCommerce.demo.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ResponseDto {
    private String responseCode;
    private String responseMessage;
    private Object response;
}
