package tests;

import api.AuthApi;
import dto.request.CreateUserRequest;
import dto.response.CreateUserResponse;
import dto.response.ErrorResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import utils.DataGenerator;
import utils.TestConfig;

import static org.junit.Assert.*;

@DisplayName("Тесты на создание пользователя")
public class CreateUserTest {

    private String accessToken;
    private String userEmail;

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
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешной регистрации нового пользователя")
    public void createUniqueUserSuccessfully() {
        CreateUserRequest userRequest = DataGenerator.generateRandomUser();
        userEmail = userRequest.getEmail();

        Response response = AuthApi.register(userRequest);

        assertEquals("Статус код должен быть 200",
                TestConfig.STATUS_OK,
                response.getStatusCode());

        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);

        assertTrue("Флаг success должен быть true",
                createUserResponse.isSuccess());
        assertNotNull("AccessToken не должен быть null",
                createUserResponse.getAccessToken());
        assertNotNull("RefreshToken не должен быть null",
                createUserResponse.getRefreshToken());
        assertNotNull("Объект user не должен быть null",
                createUserResponse.getUser());
        assertEquals("Email пользователя должен совпадать",
                userRequest.getEmail(),
                createUserResponse.getUser().getEmail());
        assertEquals("Имя пользователя должно совпадать",
                userRequest.getName(),
                createUserResponse.getUser().getName());

        accessToken = createUserResponse.getAccessToken();
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка ошибки при попытке регистрации уже существующего пользователя")
    public void createExistingUserReturnsError() {
        CreateUserRequest firstUser = DataGenerator.generateRandomUser();
        userEmail = firstUser.getEmail();

        Response firstResponse = AuthApi.register(firstUser);
        assertEquals("Первый пользователь должен создаться успешно",
                TestConfig.STATUS_OK,
                firstResponse.getStatusCode());

        CreateUserResponse firstUserResponse = firstResponse.as(CreateUserResponse.class);
        accessToken = firstUserResponse.getAccessToken();

        Response secondResponse = AuthApi.register(firstUser);

        assertEquals("Статус код должен быть 403",
                TestConfig.STATUS_FORBIDDEN,
                secondResponse.getStatusCode());

        ErrorResponse errorResponse = secondResponse.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "User already exists",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Проверка ошибки при регистрации без обязательного поля email")
    public void createUserWithoutEmailReturnsError() {
        CreateUserRequest userRequest = DataGenerator.generateUserWithoutField("email");

        Response response = AuthApi.register(userRequest);

        assertEquals("Статус код должен быть 403",
                TestConfig.STATUS_FORBIDDEN,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "Email, password and name are required fields",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Проверка ошибки при регистрации без обязательного поля password")
    public void createUserWithoutPasswordReturnsError() {
        CreateUserRequest userRequest = DataGenerator.generateUserWithoutField("password");

        Response response = AuthApi.register(userRequest);

        assertEquals("Статус код должен быть 403",
                TestConfig.STATUS_FORBIDDEN,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "Email, password and name are required fields",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Проверка ошибки при регистрации без обязательного поля name")
    public void createUserWithoutNameReturnsError() {
        CreateUserRequest userRequest = DataGenerator.generateUserWithoutField("name");

        Response response = AuthApi.register(userRequest);

        assertEquals("Статус код должен быть 403",
                TestConfig.STATUS_FORBIDDEN,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "Email, password and name are required fields",
                errorResponse.getMessage());
    }
}
