package api;

import dto.request.CreateUserRequest;
import dto.request.LoginRequest;
import io.restassured.response.Response;
import utils.ApiClient;
import utils.TestConfig;

public class AuthApi {

    public static Response register(CreateUserRequest userRequest) {
        return ApiClient.post(TestConfig.REGISTER_PATH, userRequest);
    }

    public static Response register(String email, String password, String name) {
        CreateUserRequest userRequest = new CreateUserRequest(email, password, name);
        return register(userRequest);
    }

    public static Response login(LoginRequest loginRequest) {
        return ApiClient.post(TestConfig.LOGIN_PATH, loginRequest);
    }

    public static Response login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        return login(loginRequest);
    }

    public static Response deleteUser(String accessToken) {
        return ApiClient.deleteWithAuth("/auth/user", accessToken);
    }
}
