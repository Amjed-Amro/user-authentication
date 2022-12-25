package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.handlers.interfaces.ResponseHandler;
import org.springframework.stereotype.Service;

@Service
public class ResponseHandlerImpl implements ResponseHandler {
    @Override
    public ResponseDto buildApiResponse(Object result, Boolean isSuccess) {
        if (isSuccess) {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, result);
        }
        return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, result);
    }
}
