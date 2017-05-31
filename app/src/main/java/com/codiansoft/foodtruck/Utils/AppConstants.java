package com.codiansoft.foodtruck.Utils;

/**
 * Constant values for VLB .
 */
public class AppConstants {

    public static final String PREF_NAME = "com.project.captor";

    public static final int SUCCESS_CODE = 200;
    public static final int UNSUCCESS_CODE = 400;
    public static final int OLD_USER_RESPONSE_CODE = 201;
    public static final int USERNAME_EXIST = 409;
    public static final int NO_MARKET_FOUND = 404;

    public static final int NAME_FIREND = 2;
    public static final int NAME_PUBLIC = 64;
    public static final int EMAIL_FIREND = 8;
    public static final int EMAIL_PUBLIC = 256;
    public static final int ADDRESS_FIREND = 32;
    public static final int ADDRESS_PUBLIC = 1024;
    public static final int PICTURE_FIREND = 4;
    public static final int PICTURE_PUBLIC = 128;

    public static final int FRIEND_ONLY = 46;
    //
    public static final String API_ID = "API_ID";
    public static final String API_SECRECT = "API_SECRECT";
    public static final String LOGGEDIN = "LOGGEDIN";
    public static final String USER_ID = "USER_ID";
    public static final String IS_CUSTOMER = "IS_CUSTOMER";
    public static final String APP_MAIN_DIR = "Saudi Truck";


    // server constants
    public static final String IMAGE_URL = "http://www.appcaptor.com/foodtruck/assets/profile_pic/";
    public static final String SERVER_URL = "http://www.appcaptor.com:80/foodtruck/api/";
    public static final String USER_REGISTER = SERVER_URL + "user/register";
    public static final String LOGIN = SERVER_URL + "user/login";
    public static final String SET_LOCATION = SERVER_URL + "user/location";
    public static final String GET_TRUCKS = SERVER_URL + "user/active_truckowners";
    public static final String UPDATE_PROFILE = SERVER_URL + "user/update";
    public static final String GET_MENU = SERVER_URL + "menu/list";
    public static final String GET_BOOKINGS = SERVER_URL + "bookings/get_bookings";
    public static final String ADD_MENU = SERVER_URL + "menu/new";
    public static final String ADD_BOOING = SERVER_URL + "bookings/new_booking";
    public static final String UPDATE_BOOING = SERVER_URL + "bookings/update_booking";
    public static final String UPDATE_MENU = SERVER_URL + "menu/update";
    public static final String DELETE_MENU = SERVER_URL + "menu/delete";
    public static final String NEW_ORDER = SERVER_URL + "orders/new_order";
    public static final String GET_ORDER = SERVER_URL + "orders/get_orders";
    public static final String ACCEPT_ORDERS = SERVER_URL + "orders/update_order";
    public static final String ADD_GROUP_MEMBER = SERVER_URL + "group/add_member";
    public static final String ACCEPT_GROUP = SERVER_URL + "group/accept";
    public static final String REJECT_GROUP = SERVER_URL + "group/reject";
    public static final String MAIN_DIR = "Captor";
    public static final String PROFILE_IMAGE_BASE = "PROFILE_IMAGE_BASE";
    public static final String CUSTOMERINFO = SERVER_URL + "user/customer_info";
    public static final String RESET_PASSWORD = SERVER_URL + "user/forgetPassword";
    public static final String RECOVERPASSWORD = SERVER_URL + "user/RecoverPassword";
    //Login type : 1 for new user 2 for old usera

    public static final String LOGIN_TYPE = "LOGIN_TYPE";

    // profile
    public static final String PROFILE_NAME = "PROFILE_NAME";
    public static final String PROFILE_IMAGE_NAME = "My Profile Picture.jpg";

    public static final String GROUP_IMAGE_NAME = "GROUP_IMAGE_NAME.jpg";
    public static final String ADDCONTACT = "ADDCONTACT";


    //Groupds

    public static final String ADD_GROUP = SERVER_URL + "group/create";
    public static final String GET_ALL_GROUPT = SERVER_URL + "group/get_all";
    public static final String GET_NOTIFICATION = SERVER_URL + "group/notifications";

    //Contacts
    public static final String UPLOAD_CONTACTS = SERVER_URL + "contact/sync";
    public static final String ACCEPT_ACTION = "ACCEPT_ACTION";
    public static final String REJECT_ACTION = "REJECT_ACTION";
    public static final String FIND_CONTACT = SERVER_URL + "contact/search";

    public static final String YES_ACTION = "YES_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";
    public static final String SUCCESS_MSG = "SUCCESS_MSG";
    public static final String UNSUCCESS_MSG = "UNSUCCESS_MSG";

    //
    public static final String SELECTED_GROUP = "SELECTED_GROUP";
    public static final String SELECTED_GROUP_ADMIN = "SELECTED_GROUP_ADMIN";

    //GCM

    public static final String GCM_REGISTRATION_KEY = "GCM_REGISTRATION_KEY";

    //Setting

    // 0 for edit name , 1 for edit email, 2 for edit address
    public static final String SETTING_EDIT_OPTION = "SETTING_EDIT_OPTION";


    public static final String PHOTO_URI = "PHOTO_URI";
    public static final String PHOTO_URI2 = "PHOTO_URI2";
    public static String OrderTime = "2016-04-21 07:03:39";
}
