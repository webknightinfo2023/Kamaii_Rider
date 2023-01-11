package com.kamaii.rider.interfacess;


public interface Consts {
    String APP_NAME = "Kamaii Partner";
    //old 677440
    String BASE_URL = "https://kamaii.in/app/Webservicerider/";
    String BASE_URL2 = "https://kamaii.in/app/Webservice_rider/";
    String BASE_URL3 = "https://kamaii.in/app/Webservice_rider_new_flow/";
    String IMAGE_URL = "https://kamaii.in/app/";
    String PAYMENT_FAIL = "https://kamaii.in/app/Stripe/Payment/fail";
    String PAYMENT_SUCCESS = "https://kamaii.in/app/Stripe/Payment/success";
    String MAKE_PAYMENT = "https://kamaii.in/app/Stripe/Payment/make_payment/";
    String RIDER_PICKUP_RING = "https://kamaii.in/app/assets/images/audio/rider_pickup_time.mp3";

    String PAYMENT_FAIL_Paypal = "https://kamaii.in/app/payufailure/payufailure";
    String PAYMENT_SUCCESS_paypal = "https://kamaii.in/app/payufailure/payusuccess";
    String MAKE_PAYMENT_paypal = "https://kamaii.in/app/payufailure/paypalWallent?";
    String PRIVACY_URL = "https://kamaii.in/app/payufailure/privacy";
    String TERMS_URL = "https://kamaii.in/app/payufailure/term";
    String About_URL = "https://kamaii.in/app/Myadmin/about";
    String Help_URL = "https://kamaii.in/app/Webservice/patner_help";
    String COVID_URL = "https://kamaii.in/app/Myadmin/covid19";

    public static final int DRAW_PATH_CLIENT = 25;
    double DISCOUNT_RATE = 10;
    /*Api Details*/


    String merchantIdSandbox = "595846726305054";
    String accessTokenSandbox = "03B4ACAF59389B18C8C3C58B4E7CDD93";


    String merchantIdLIVE = "230782735516696";
    //    String accessTokenLIVE = "508E415CF7A4600CFFA7EFD504786BA4";
    //String accessTokenLIVE = "4972715CDB70A71C57362E1C157690F8";
    String accessTokenLIVE = "0F1BF36D505CDA59B0820D0C77CA6B1C";

    String EARNING_DATA_API = "rider_earning_data";

    String LOGIN_API = "signIn2";
    String PAYMENT_STATUS = "checkwallet";
    String HOME_ADDRESS_LONGI = "HOME_ADDRESS_LONGI";
    String Add_REFER = "add_referral";
    String VIEW_REFER = "view_referral";
    String VIEW_REFER_SERVICE = "get_referra_det";
    String REGISTER_API = "SignUp";
    String SEND_OTP = "send_otp";
    String IS_VARIFIED = "is_verified";

    String CREATE_LOG = "create_log";
    String CANCEL_REQUEST_LOG = "create_log_cancel";
    String GET_CANCEL_REASON_API = "get_cancel_reason";
    String SEND_REACH_NOTIFICATION = "rider_aa_raha_hai";
    String GET_REFERRAL_CODE_API = "getMyReferralCode";
    String GET_CHAT_HISTORY_API = "getChatHistoryForArtist";
    String GET_CHAT_API = "getChat";
    String MY_EARNING1_USER = "my_earning_user";
    String SEND_CHAT_API = "sendmsg";
    String GET_NOTIFICATION_API = "getNotifications";
    String GET_ARTIST_BY_ID_API = "getArtistByid";
    String UPDATE_PROFILE_API = "editPersonalInfo";
    String Add_PARTER_DOC = "addPartnerDocuments";
    String UPDATE_FIVESTATUS = "basicinfostep5";
    String GET_TVIDEO2 = "getVideo2";
    String ONLINE_RIDER_IMAGE = "rider_login";

    String UPDATE_PROFILE_ARTIST_API = "artistPrsonalInfo";
    String GET_ALL_CATEGORY_API = "getAllCaegory";
    String GET_ALL_CATEGORY_API2 = "getAllCaegory2";
    String GET_INCENTIVE_DATA_API = "today_hour_earning_data";

    String GET_ALL_VEHICLE_LIST_API = "getVehiclelist";
    String GET_ALL_VEHICLE_DISPLAY_LIST = "getVehiclelist2";
    String ADD_VEHICLE_API = "addVehiclelist";
    String SEND_VEHICLE_STATUS_API = "onnoffvehiclelist";
    String GET_SUBCAT_IMAGE_API = "getAllservice2";
    String GET_ALL_SKILLS_BY_CAT_API = "getSkillsByCategory";
    String ADD_QUALIFICATION_API = "addQualification";
    String ADD_PRODUCT_API = "addProduct";
    String ADD_SERVICE_SHIPPING = "add_service_shipping";
    String ADD_GALLERY_API = "addGallery";
    String GET_INVOICE_API = "getMyInvoice";
    String CURRENT_BOOKING_API = "getMyCurrentBooking";
    String BOOKING_OPERATION_API = "booking_operation";
    //String DECLINE_BOOKING_API = "decline_booking";
    String PICKUP_BOOKING_OPERATION_API = "booking_pickup_img";
    String OFFLINE_IMG_API = "booking_img";
    String OFFLINE_IMG_API_NEW = "booking_img_new";
    String DECLINE_BOOKING_API = "decline_booking2_click";
    String DECLINE_ULTIMATE_BOOKING_API = "decline_booking2_ultimate_click";
    String ONLINE_OFFLINE_API = "onlineOffline";
    String UPDATE_LOCATION_API = "updateLocation";
    String ARTIST_LOGOUT_API = "artistLogout";
    String GET_MY_TICKET_API = "getMyTicket";
    String GENERATE_TICKET_API = "generateTicket";
    String GET_TICKET_COMMENTS_API = "getTicketComments";
    String ADD_TICKET_COMMENTS_API = "addTicketComments";
    String FORGET_PASSWORD_API = "forgotPassword";
    String GET_APPOINTMENT_API = "getAppointment";
    String EDIT_APPOINTMENT_API = "edit_appointment";
    String GET_WHATS_APP_CHAT_API = "get_chat_api";
    String APPOINTMENT_OPERATION_API = "appointment_operation";
    String GET_ALL_JOB_API = "get_all_job";
    String APPLIED_JOB_API = "applied_job";
    String JOB_STATUS_ARTIST_API = "job_status_artist";
    String GET_APPLIED_JOB_ARTIST_API = "get_applied_job_artist";
    String CHANGE_COMMISSION_ARTIST_API = "changeCommissionArtist";
    String START_JOB_API = "startJob";
    String DELETE_PROFILE_IMAGE_API = "deleteProfileImage";
    String MY_EARNING1_API = "myEarning1";
    String WALLET_REQUEST_API = "walletRequest";
    String ADD_MONEY_API = "addMoney";
    String GET_WALLET_HISTORY_API = "getWalletHistory";
    String GET_WALLET_API = "getWallet";
    String DELETE_GALLERY_API = "deleteGallery";
    String DELETE_PRODUCT_API = "deleteProduct";
    String DELETE_PRODUCT_SERVICE = "delete_product_service";
    String serviceVisiblity = "serviceVisiblity";
    String EDIT_PRODUCT_API = "edit_product";
    String UPDATE_ADD_ADDRESS = "add_user_address2";
    String UPDATE_LOCATION_BY_RIDER = "update_location_from_rider";
    String PAYMENT_FRAGMENT_API = "getcashdepositdetail";

    String GET_BUSINESS_CARD_IMAGES = "getriderbanner";
    String GET_BIRTHDAY_CARD_IMAGES = "getriderbanner2";
    String REQUEST_AMOUNT_TO_PARTNER = "request_amount_to_partner";
    String FOOD_NOT_READY = "foods_not_ready";

    String UPDATE_QUALIFICATION_API = "updateQualification";
    String DELETE_QUALIFICATION_API = "deleteQualification";
    String GET_ALL_BOOKING_ARTIST_API = "getAllBookingArtist";
    String GET_ALL_BOOKING_ARTIST_OLD_API = "getAllBookingArtistoldapi";
    String GET_ALL_BOOKING_ARTIST__CAB_API = "getAllBookingArtistCab";
    String GET_TVIDEO = "getVideo";
    String GET_VERSION = "get_version2";
    String GET_APPROVAL_STATUS_API = "getApprovalStatus";
    String GET_ALL_SUB_CATEGORY_API = "get_sub_cat";
    String GET_SERVICE_SUB_CATEGORY_API = "get_sub_cat3";
    String get_sublevel_cat = "get_sublevel_cat";
    String getAllservice = "getAllservice";
    String GET_RATE = "get_sel_box";
    String VERIFY_API = "verify_otp";
    String UPDATE_CATEGORY_ID = "basicinfostep1";
    String UPDATE_APPROVAL = "final_submit_for_approval";
    String CHECK_APPROVE = "check_approve";
    String GET_DEMO_BOOKING_CATEGORY_API = "getDemoBookingCaegory";
    String GET_RIDER_HOMEPAGE_DATA = "getRiderhomepagedata";
    String GET_GUIDE_SEQUENCE = "getguidestep";
    //String DECLINE_BOOKING_API2 = "decline_booking2";
    String DECLINE_BOOKING_API2 = "decline_booking2_auto";

    /*app data*/
    String CAMERA_ACCEPTED = "camera_accepted";
    String STORAGE_ACCEPTED = "storage_accepted";
    String MODIFY_AUDIO_ACCEPTED = "modify_audio_accepted";
    String CALL_PRIVILAGE = "call_privilage";
    String FINE_LOC = "fine_loc";
    String CORAS_LOC = "coras_loc";
    String READ_STORAGE = "readstorage";
    String WACK_LOCK = "wack_lock";
    String BACKGROUND = "background";
    String INTERNAL_WINDOW = "internal_window";

    String CALL_PHONE = "call_phone";
    String PAYMENT_URL = "payment_url";
    String SURL = "surl";
    String FURL = "furl";
    String ARTIST_DTO = "artist_dto";
    String CATEGORY_list = "category_list";
    String SCREEN_TAG = "screen_tag";
    String EXTRA_PERMISSION_GRANT = "permission_extra";
    String HISTORY_DTO = "history_dto";
    String MY_LOCATION = "my_location";
    String MAX_PRICE = "maxprice";
    String SHIPPING = "shipping";
    String SUB_CAT_ID = "sub_cat_id";
    String GET_SERVICE_SHIPPING = "get_service_shipping";
    String GET_ALL_SERVICE_SHIPPING = "get_all_service_shipping";

    String ADD_SERVICE = "add_service";
    String ADD_CUSTOM_SERVICE = "add_new_service";
    String SERVICE_ID = "service_id";

    String GET_ALL_ARTISTS_API = "getAllArtists";
    String GET_OFFLINE_REASON_API = "get_offline_reason";
    /*app data*/

    /*Project Parameter*/
    String ARTIST_ID = "artist_id";
    String RIDER_ID = "rider_id";
    String SELECTED_SERVICE_ID = "selected_service";
    String CHAT_LIST_DTO = "chat_list_dto";
    String USER_DTO = "user_dto";
    String IS_REGISTERED = "is_registered";
    String IMAGE_URI_CAMERA = "image_uri_camera";
    String DATE_FORMATE_SERVER = "EEE, MMM dd, yyyy hh:mm a"; //Wed, JUL 06, 2018 04:30 pm
    String DATE_FORMATE_TIMEZONE = "z";
    String BROADCAST = "broadcast";
    String LOCATION1 = "location1";
    String PRODUCT_ID = "id";
    String service_id = "service_id";
    String is_visible = "is_visible";
    String BOOKING_FLAG = "booking_flag";
    String API_NAME = "api_name";
    String API_PARAMS = "api_params";


    /*Parameter Get Artist and Search*/
    String USER_ID = "user_id";
    String PACKAGE_LIST = "package_list";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String ID = "id";
    /*Get All History*/
    String ROLE = "role";
    String INVOICEID = "invoice_id";
    String SERVICE_PRICE = "service_price";

    String SUB_CATEGORY_ID = "sub_category_id";
    String THIRACATEGORY = "thirdcategory";
    /*Send Message*/
    String MESSAGE = "message";
    String SEND_BY = "send_by";
    String SENDER_NAME = "sender_name";


    /*Login Parameter*/
    String NAME = "name";
    String EMAIL_ID = "email_id";
    String PASSWORD = "password";
    String DEVICE_TYPE = "device_type";
    String DEVICE_TOKEN = "device_token";
    String TESTING_ACCOUNT = "testing_account_otp";
    String MAP_TRACKER = "map_tracker";
    String DEVICE_ID = "device_id";
    String REFERRAL_CODE = "referral_code";


    /*Update Profile*/
    String NEW_PASSWORD = "new_password";
    String GENDER = "gender";
    String MOBILE = "mobile";
    String OTP = "OTP";
    String OFFICE_ADDRESS = "office_address";
    String ADDRESS = "address";
    String IMAGE = "image";

    /*Update Profile Artist*/
    String CATEGORY_ID = "category_id";
    String sub_category_id = "sub_category_id";
    String RATE = "rate";
    String BIO = "bio";
    String LOCATION = "location";
    String PRICE = "price";
    String DESC = "description";
    String ABOUT_US = "about_us";
    String SKILLS = "skills";
    String VIDEO_URL = "video_url";
    String CITY = "city";
    String COUNTRY = "country";
    String BANNER_IMAGE = "banner_image";
    String BANK_NAME = "bank_name";
    String ACCOUNT_HOLDER_NAME = "benifeciry_name";
    String ACCOUNT_NO = "account_no";
    String IFSC_CODE = "ifsc_code";
    String SERVICE_CHARGE = "service_charge";
    String QUANTITYDAYS = "quantitydays";
    String MAXIMUMVALUE = "maxmiumvalue";
    String partner_number = "partner_number";
    String ROUNDTRIP = "roundtrip";
    String GET_TODAYS_EARNING = "getPartnerWalletDetail";

    /*Invoice*/
    String GET_INVOICE_API2 = "getMyInvoice2";


    String MONEY = "money";
    /*Get Skills*/
    String CAT_ID = "cat_id";


    /*Update Qualification*/
    String TITLE = "title";
    String DESCRIPTION = "description";
    String QUALIFICATION_ID = "qualification_id";

    /*Update Qualification*/
    String PRODUCT_NAME = "product_name";
    String PRODUCT_IMAGE = "product_image";


    /*Booking Opreations*/
    String BOOKING_ID = "booking_id";
    String REQUEST = "request";
    String APPROXTIME = "approxdatetime";

    /*Decline*/
    String DECLINE_BY = "decline_by";
    String DECLINE_REASON = "decline_reason";

    /*Online Offline*/
    String IS_ONLINE = "is_online";

    /*Add Ticket*/
    String REASON = "reason";


    /*Get Ticket*/
    String TICKET_ID = "ticket_id";
    String COMMENT = "comment";

    /*Edit Appointment*/
    String APPOINTMENT_ID = "appointment_id";


    /*Book Artist*/

    String DATE_STRING = "date_string";
    String TIMEZONE = "timezone";


    /*Apply Job*/
    String JOB_ID = "job_id";

    /*Job Status*/
    String AJ_ID = "aj_id";
    String STATUS = "status";

    String SERVER_RESPONSE_API = "server_response";

    /*Chat*/
    String CHAT_TYPE = "chat_type";

    /*Payment Type*/
    String ARTIST_COMMISSION_TYPE = "artist_commission_type";

    /*Add Money*/
    String TXN_ID = "txn_id";
    String ORDER_ID = "order_id";
    String AMOUNT = "amount";
    String CURRENCY = "currency";

    /*Promotion Fragment*/
    String GET_SHARED_PROFILE_MSG_API = "get_profile_vendor_id2";
    String GET_SHARED_SERVICE_API = "get_vendor_service";
    String SEND_SERVICE_API = "selected_service_msg";


    /*Notifications Codes*/
    String CHAT_NOTIFICATION = "10007";//both
    String TICKET_COMMENT_NOTIFICATION = "10009";//both
    String TICKET_STATUS_NOTIFICATION = "10015";//both
    String WALLET_NOTIFICATION = "10010";//both
    String DECLINE_BOOKING_ARTIST_NOTIFICATION = "10002";//both
    String START_BOOKING_ARTIST_NOTIFICATION = "10003";//ar
    String RIDER_HAS_PICKUP_ORDER = "10050";//ar
    String BRODCAST_NOTIFICATION = "10014";//both
    String ADMIN_NOTIFICATION = "10016";//both
    String SEND_RIDER_NOTIFICATION = "10020";//both : partner and rider
    String ACCEPT_REQUEST_RIDER_NOTIFICATION = "10021";//both : partner and rider

    String VENDOR_HAS_BOOKING_END_SUCCESSFULLY = "10051";//partner and rider
    String SEND_RESPONSE_NOTIFICATION = "10115";
    String RANDOM_RIDER_NOTIFICATION = "22100";

    String BOOK_ARTIST_NOTIFICATION = "10001";//ar
    String END_BOOKING_ARTIST_NOTIFICATION = "10004";//user
    String CANCEL_BOOKING_ARTIST_NOTIFICATION = "10005";
    String ACCEPT_BOOKING_ARTIST_NOTIFICATION = "10006";//user
    String USER_BLOCK_NOTIFICATION = "1008";
    String JOB_NOTIFICATION = "10011";//ar
    String JOB_APPLY_NOTIFICATION = "10012";//user
    String DELETE_JOB_NOTIFICATION = "10013";//ar
    String APKA_ORDER_DELAY_HAI = "100125";//ar

    String TYPE = "type";
    public static final String PREF_NAME = " eDriver";

    public static final int IS_ASSIGNED = 0;
    public static final int IS_WALKER_STARTED = 1;
    public static final int IS_WALKER_ARRIVED = 2;
    public static final int IS_WALK_STARTED = 3;
    public static final int IS_WALK_COMPLETED = 4;
    public static final int IS_DOG_RATED = 5;

    public static final String URL = "url";

    public class ServiceCode {
        public static final int REGISTER = 1;
        public static final int LOGIN = 2;
        public static final int GET_ALL_REQUEST = 3;
        public static final int RESPOND_REQUEST = 4;
        public static final int CHECK_REQUEST_STATUS = 5;
        public static final int REQUEST_IN_PROGRESS = 6;
        public static final int WALKER_STARTED = 7;
        public static final int WALKER_ARRIVED = 8;
        public static final int WALK_STARTED = 9;
        public static final int WALK_COMPLETED = 10;
        public static final int RATING = 11;
        public static final int GET_ROUTE = 12;
        public static final int APPLICATION_PAGES = 13;
        public static final int UPDATE_PROFILE = 14;
        public static final int GET_VEHICAL_TYPES = 16;
        public static final int FORGET_PASSWORD = 17;
        public static final int HISTORY = 18;
        public static final int CHECK_STATE = 19;
        public static final int TOGGLE_STATE = 20;
        public static final int PATH_REQUEST = 21;
        public static final int DRAW_PATH_ROAD = 22;
        public static final int DRAW_PATH = 23;
        public static final int LOGOUT = 24;
        public static final int DRAW_PATH_CLIENT = 25;
        public static final int GET_DURATION = 26;
    }

    public static final long DELAY = 0;
    public static final long TIME_SCHEDULE = 10 * 1000;
    public static final long DELAY_OFFLINE = 15 * 60 * 1000;
    public static final long TIME_SCHEDULE_OFFLINE = 15 * 60 * 1000;
    public static final int NO_REQUEST = -1;
    public static final int NO_TIME = -1;
    public static final String NEW_REQUEST = "new_request";
    public static final String CANCEL_REQUEST = "CANCEL_REQUEST";
    public static final int NOTIFICATION_ID = 0;

    public class Params {
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String FIRSTNAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String PHONE = "phone";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String DEVICE_TYPE = "device_type";
        public static final String BIO = "bio";
        public static final String ADDRESS = "address";
        public static final String STATE = "state";
        public static final String COUNTRY = "country";
        public static final String ZIPCODE = "zipcode";
        public static final String TAXI_MODEL = "car_model";
        public static final String TAXI_NUMBER = "car_number";
        public static final String TIMEZONE = "timezone";
        public static final String LOGIN_BY = "login_by";
        public static final String SOCIAL_UNIQUE_ID = "social_unique_id";
        public static final String PICTURE = "picture";
        public static final String ID = "id";
        public static final String TOKEN = "token";
        public static final String REQUEST_ID = "request_id";
        public static final String ACCEPTED = "accepted";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String DISTANCE = "distance";
        public static final String BEARING = "bearing";
        public static final String COMMENT = "comment";
        public static final String RATING = "rating";
        public static final String INCOMING_REQUESTS = "incoming_requests";
        public static final String TIME_LEFT_TO_RESPOND = "time_left_to_respond";
        public static final String REQUEST = "request";
        public static final String REQUESTS = "requests";
        public static final String REQUEST_DATA = "request_data";
        public static final String NAME = "name";
        public static final String NUM_RATING = "num_rating";
        public static final String OWNER = "owner";
        public static final String WALKER = "walker";
        public static final String UNIQUE_ID = "unique_id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String INFORMATIONS = "informations";
        public static final String IS_ACTIVE = "is_active";
        public static final String ICON = "icon";
        public static final String TYPE = "type";
        public static final String DISTANCE_COST = "distance_cost";
        public static final String TIME_COST = "time_cost";
        public static final String TOTAL = "total";
        public static final String IS_PAID = "is_paid";
        public static final String TIME = "time";
        public static final String DATE = "date";
        public static final String LOCATION_DATA = "locationdata";
        public static final String START_TIME = "start_time";
        public static final String PAYMENT_TYPE = "payment_type";
        public static final String IS_APPROVED = "is_approved";
        public static final String IS_CANCELLED = "is_cancelled";
        public static final String OLD_PASSWORD = "old_password";
        public static final String NEW_PASSWORD = "new_password";
        public static final String CAR_NUMBER = "car_number";
        public static final String CAR_MODEL = "car_model";
        public static final String REFERRAL_BONUS = "referral_bonus";
        public static final String PROMO_BONUS = "promo_bonus";
        public static final String UNIT = "unit";
        public static final String DESTINATION_LATITUDE = "dest_latitude";
        public static final String DESTINATION_LONGITUDE = "dest_longitude";
        public static final String FROM_DATE = "from_date";
        public static final String TO_DATE = "to_date";
        public static final String SOURCE_LAT = "start_lat";
        public static final String SOURCE_LONG = "start_long";
        public static final String DEST_LAT = "end_lat";
        public static final String DEST_LONG = "end_long";
        public static final String MAP_URL = "map_url";
        public static final String SRC_ADD = "src_address";
        public static final String DEST_ADD = "dest_address";
        public static final String CURRENCY = "currency";
        public static final String DEST_ADDRESS = "dest_address";
    }
}
