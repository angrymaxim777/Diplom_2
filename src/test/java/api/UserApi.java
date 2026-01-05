package api;

import io.restassured.response.Response;
import utils.ApiClient;
import utils.TestConfig;

public class UserApi {

    public static Response getUserData(String accessToken) {
        return ApiClient.getWithAuth("/auth/user", accessToken);
    }

    public static Response updateUserData(Object userData, String accessToken) {
        return ApiClient.postWithAuth("/auth/user", userData, accessToken);
    }
}
