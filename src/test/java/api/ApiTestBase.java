package api;

import dto.response.CreateUserResponse;
import dto.response.ErrorResponse;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import utils.DataGenerator;
import utils.TestConfig;

public class ApiTestBase {

    protected String accessToken;
    protected String userEmail;
    protected String userPassword;
    protected String userName;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    protected CreateUserResponse createTestUser() {
        userEmail = DataGenerator.generateUniqueEmail();
        userPassword = DataGenerator.generatePassword();
        userName = DataGenerator.generateName();

        Response response = AuthApi.register(userEmail, userPassword, userName);

        if (response.getStatusCode() == TestConfig.STATUS_OK) {
            return response.as(CreateUserResponse.class);
        } else {
            ErrorResponse error = response.as(ErrorResponse.class);
            throw new RuntimeException("Не удалось создать тестового пользователя: " + error.getMessage());
        }
    }

    protected String getAccessTokenForTestUser() {
        CreateUserResponse userResponse = createTestUser();
        return userResponse.getAccessToken();
    }
}
