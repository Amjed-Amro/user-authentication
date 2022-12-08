package com.eCommerce.demo.constants;

public class Constants {

    public final static String USERNAME_NOT_FOUND_MSG = "user with email %s not found ";
    public final static String EMAIL_ALREADY_IN_USE = "the email %s cant be used";
    public final static String PRODUCTS_URL = "https://dummyjson.com/products";
    public static final String NOTHING_FOUND = "nothing found";
    public static final String SYSTEM_ERROR = "System Error, please try again later ";
    public static final String APPLICATION_CONTROLLER_ERROR = "ERROR Processing Your Request";


    public static class RESPONSE_CODE {
        public static final String SUCCESS = "000";
        public static final String FAILED = "999";
    }

    public static class RESPONSE_MESSAGE {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILED = "ERROR";
    }
}
