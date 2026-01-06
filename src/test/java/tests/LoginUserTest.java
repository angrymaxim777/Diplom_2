package tests;

import api.AuthApi;
import dto.request.CreateUserRequest;
import dto.request.LoginRequest;
import dto.response.CreateUserResponse;
import dto.response.ErrorResponse;
import dto.response.LoginResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DataGenerator;
import utils.TestConfig;

import static org.junit.Assert.*;

@DisplayName("Тесты на авторизацию пользователя")
public class LoginUserTest {

    private String userEmail;
    private String userPassword;
    private String userName;
    private String accessToken;

    @Before
    public void setUp() {
        CreateUserRequest userRequest = DataGenerator.generateRandomUser();
        userEmail = userRequest.getEmail();
        userPassword = userRequest.getPassword();
        userName = userRequest.getName();

        Response response = AuthApi.register(userRequest);
        assertEquals("Пользователь должен создаться успешно",
                TestConfig.STATUS_OK,
                response.getStatusCode());

        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
        accessToken = createUserResponse.getAccessToken();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            try {
                Response deleteResponse = AuthApi.deleteUser(accessToken);
                if (deleteResponse.getStatusCode() == TestConfig.STATUS_OK) {
                    System.out.println("Пользователь успешно удален: " + userEmail);
                }
            } catch (Exception e) {
                System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешного входа с корректными учетными данными")
    public void loginWithExistingUserSuccessfully() {
        LoginRequest loginRequest = new LoginRequest(userEmail, userPassword);

        Response response = AuthApi.login(loginRequest);

        assertEquals("Статус код должен быть 200",
                TestConfig.STATUS_OK,
                response.getStatusCode());

        LoginResponse loginResponse = response.as(LoginResponse.class);

        assertTrue("Флаг success должен быть true",
                loginResponse.isSuccess());
        assertNotNull("AccessToken не должен быть null",
                loginResponse.getAccessToken());
        assertNotNull("RefreshToken не должен быть null",
                loginResponse.getRefreshToken());
        assertNotNull("Объект user не должен быть null",
                loginResponse.getUser());
        assertEquals("Email пользователя должен совпадать",
                userEmail,
                loginResponse.getUser().getEmail());
        assertEquals("Имя пользователя должно совпадать",
                userName,
                loginResponse.getUser().getName());
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неверным паролем")
    public void loginWithWrongPasswordReturnsError() {
        String wrongPassword = "wrong" + userPassword;
        LoginRequest loginRequest = new LoginRequest(userEmail, wrongPassword);

        Response response = AuthApi.login(loginRequest);

        assertEquals("Статус код должен быть 401",
                TestConfig.STATUS_UNAUTHORIZED,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "email or password are incorrect",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Проверка ошибки при входе с неверным email")
    public void loginWithWrongEmailReturnsError() {
        String wrongEmail = "wrong" + userEmail;
        LoginRequest loginRequest = new LoginRequest(wrongEmail, userPassword);

        Response response = AuthApi.login(loginRequest);

        assertEquals("Статус код должен быть 401",
                TestConfig.STATUS_UNAUTHORIZED,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "email or password are incorrect",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Вход без заполнения поля email")
    @Description("Проверка ошибки при входе без email")
    public void loginWithoutEmailReturnsError() {
        LoginRequest loginRequest = new LoginRequest(null, userPassword);

        Response response = AuthApi.login(loginRequest);

        assertEquals("Статус код должен быть 401",
                TestConfig.STATUS_UNAUTHORIZED,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "email or password are incorrect",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Вход без заполнения поля password")
    @Description("Проверка ошибки при входе без password")
    public void loginWithoutPasswordReturnsError() {
        LoginRequest loginRequest = new LoginRequest(userEmail, null);

        Response response = AuthApi.login(loginRequest);

        assertEquals("Статус код должен быть 401",
                TestConfig.STATUS_UNAUTHORIZED,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "email or password are incorrect",
                errorResponse.getMessage());
    }
}
