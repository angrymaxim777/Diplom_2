package utils;

public class TestConfig {

    public static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    public static final String REGISTER_PATH = "/auth/register";
    public static final String LOGIN_PATH = "/auth/login";
    public static final String ORDERS_PATH = "/orders";
    public static final String INGREDIENTS_PATH = "/ingredients";

    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int SOCKET_TIMEOUT = 5000;

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
}
