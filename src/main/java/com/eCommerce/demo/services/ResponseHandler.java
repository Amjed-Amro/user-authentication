package com.eCommerce.demo.services;

import com.eCommerce.demo.models.dto.ResponseDto;

public interface ResponseHandler {


    ResponseDto buildApiResponse(Object result, Boolean isSuccess);
}
