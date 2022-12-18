package com.eCommerce.demo.constants;

public class Constants {

    public static final String ALGORITHM_SECRET_CODE = "secret";
    public static final String SUCCESS = "success";
    public static final String PRODUCTS_URL = "https://dummyjson.com/products";
    public static final String USERNAME_NOT_FOUND_MSG = "user with email %s not found ";
    public static final String REQUEST_SUCCESS = "you request was successful";
    public static final String EMAIL_ALREADY_IN_USE = "the email %s cant be used";
    public static final String NOTHING_FOUND = "nothing found";
    public static final String SYSTEM_ERROR = "System Error, please try again later ";
    public static final String APPLICATION_CONTROLLER_ERROR = "ERROR Processing Your Request";


    public static class TOKENS {
        public static final Integer CONFIRM_TOKEN_VALIDITY_MINUTES = 15;
        public static final String CONFIRM_TOKEN_CONFIRMATION_URL = "http://localhost:8080/users/confirmToken/%S";
        public static final Integer ACCESS_TOKEN_VALIDITY_MILLI = 1*60*1000;
        public static final Integer REFRESH_TOKEN_VALIDITY_MILLI = 30*60*1000;
    }
    public static class ROLES {
        public static final String USER = "user";
        public static final String ADMIN = "admin";
        public static final String SUPER_ADMIN = "super_admin";
    }
    public static class RESPONSE_CODE {
        public static final String SUCCESS = "000";
        public static final String FAILED = "999";
    }
    public static class RESPONSE_MESSAGE {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILED = "ERROR";
    }
}
