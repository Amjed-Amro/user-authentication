package com.eCommerce.demo.constants;

public class Constants {

    public static final String ALGORITHM_SECRET_CODE = "secret";   
    public static final String PRODUCTS_URL = "https://dummyjson.com/products";
    public static final String REQUEST_SUCCESS = "you request was successful";


    public static class TOKENS {
        public static final String CONFIRMATION_TOKEN = "confirmationToken";
        public static final String ACCESS_TOKEN_START_PHRASE = "Bearer ";
        public static final String PASSWORD_RESET_TOKEN = "passwordResetToken";
        public static final String REFRESH_TOKEN = "refreshToken";
        public static final String TOKEN_CLAIM_ROLES = "roles";        
        public static final String TOKEN_CLAIM_PATH = "path";  
        public static final String CONFIRM_TOKEN_CONFIRMATION_URL = "http://localhost:8080/appUser/activation/%S";
        public static final String  RESET_PASSWORD_TOKEN_URL = "http://localhost:8080/appUser/resetPassword/%S";
        public static final Integer ACCESS_TOKEN_VALIDITY_MIN = 10;
        public static final Integer RESET_PASSWORD_TOKEN_VALIDITY_MINUTES = 15;
        public static final Integer REFRESH_TOKEN_VALIDITY_MIN = 30;
        public static final Integer CONFIRM_TOKEN_VALIDITY_MINUTES = 15;

    }
    public static class UPDATE_HISTORY {
        public static final String USER_CREATED = "user_created";
        public static final String ADD_TOKEN= "add_token";
        public static final String ACTIVATE= "activate_app_user";
        public static final String RESET_PASSWORD = "reset_password";
        public static final String CHANGE_PASSWORD = "change_password";
        public static final String CHANGE_FIRST_NAME = "change_first_name";
        public static final String CHANGE_LAST_NAME = "change_last_name";
        public static final String CHANGE_USER_NAME = "change_user_name";
        public static final String CHANGE_GENDER = "change_gender";
        public static final String CHANGE_AGE = "change_age";
        public static final String DEACTIVATE_USER = "deactivate_user";
        public static final String UNLOCK_USER = "unlock_user";
        public static final String LOCK_USER = "lock_user";
        public static final String SET_ACCOUNT_EXPIRED = "set_account_expired";
        public static final String SET_ACCOUNT_NON_EXPIRED = "set_account_non_expired";
        public static final String SET_CREDENTIALS_EXPIRED = "set_credentials_expired";
        public static final String SET_CREDENTIALS_NON_EXPIRED = "set_credentials_non_expired";
        public static final String ADD_ROLE = "add_role";
        public static final String REMOVE_ROLE = "remove_role";
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
