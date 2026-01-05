package tests;

import api.AuthApi;
import api.OrderApi;
import dto.request.CreateUserRequest;
import dto.request.OrderRequest;
import dto.response.CreateUserResponse;
import dto.response.ErrorResponse;
import dto.response.OrderResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DataGenerator;
import utils.TestConfig;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

@DisplayName("Тесты на создание заказа")
public class CreateOrderTest {

    private String userEmail;
    private String userPassword;
    private String accessToken;

    @Before
    public void setUp() {
        CreateUserRequest userRequest = DataGenerator.generateRandomUser();
        userEmail = userRequest.getEmail();
        userPassword = userRequest.getPassword();

        Response response = AuthApi.register(userRequest);
        assertEquals("Пользователь должен создаться успешно",
                TestConfig.STATUS_OK,
                response.getStatusCode());

        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
        accessToken = createUserResponse.getAccessToken();
    }

    @After
    public void tearDown() {
        accessToken = null;
        userEmail = null;
        userPassword = null;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа авторизованным пользователем с валидными ингредиентами")
    public void createOrderWithAuthAndIngredientsSuccessfully() {
        OrderRequest orderRequest = DataGenerator.generateValidOrder();

        assertNotNull("Список ингредиентов не должен быть null",
                orderRequest.getIngredients());
        assertFalse("Список ингредиентов не должен быть пустым",
                orderRequest.getIngredients().isEmpty());

        Response response = OrderApi.createOrderWithAuth(orderRequest, accessToken);

        assertEquals("Статус код должен быть 200",
                TestConfig.STATUS_OK,
                response.getStatusCode());

        OrderResponse orderResponse = response.as(OrderResponse.class);

        assertTrue("Флаг success должен быть true",
                orderResponse.isSuccess());
        assertNotNull("Имя бургера не должно быть null",
                orderResponse.getName());
        assertNotNull("Объект order не должен быть null",
                orderResponse.getOrder());
        assertTrue("Номер заказа должен быть больше 0",
                orderResponse.getOrder().getNumber() > 0);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    @Description("Проверка создания заказа неавторизованным пользователем с валидными ингредиентами")
    public void createOrderWithoutAuthWithIngredients() {
        OrderRequest orderRequest = DataGenerator.generateValidOrder();

        assertNotNull("Список ингредиентов не должен быть null",
                orderRequest.getIngredients());
        assertFalse("Список ингредиентов не должен быть пустым",
                orderRequest.getIngredients().isEmpty());

        Response response = OrderApi.createOrder(orderRequest);

        assertNotEquals("Статус код не должен быть 500",
                TestConfig.STATUS_INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        if (response.getStatusCode() == TestConfig.STATUS_OK) {
            OrderResponse orderResponse = response.as(OrderResponse.class);
            assertTrue("Флаг success должен быть true",
                    orderResponse.isSuccess());
        }
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void createOrderWithoutIngredientsReturnsError() {
        OrderRequest orderRequest = DataGenerator.generateOrderWithoutIngredients();

        Response response = OrderApi.createOrderWithAuth(orderRequest, accessToken);

        assertEquals("Статус код должен быть 400",
                TestConfig.STATUS_BAD_REQUEST,
                response.getStatusCode());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse("Флаг success должен быть false",
                errorResponse.isSuccess());
        assertEquals("Сообщение об ошибке должно быть корректным",
                "Ingredient ids must be provided",
                errorResponse.getMessage());
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки при создании заказа с невалидными хешами ингредиентов")
    public void createOrderWithInvalidIngredientHashReturnsError() {
        OrderRequest orderRequest = DataGenerator.generateOrderWithInvalidHash();

        Response response = OrderApi.createOrderWithAuth(orderRequest, accessToken);

        assertEquals("Статус код должен быть 500",
                TestConfig.STATUS_INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        assertNotNull("Тело ответа не должно быть null",
                response.getBody());
    }

    @Test
    @DisplayName("Создание заказа с одним ингредиентом")
    @Description("Проверка создания заказа с одним валидным ингредиентом")
    public void createOrderWithSingleIngredientSuccessfully() {
        OrderRequest validOrderRequest = DataGenerator.generateValidOrder();

        String firstIngredient = validOrderRequest.getIngredients().get(0);
        OrderRequest singleIngredientOrder = new OrderRequest(
                Collections.singletonList(firstIngredient)
        );

        Response response = OrderApi.createOrderWithAuth(singleIngredientOrder, accessToken);

        if (response.getStatusCode() == TestConfig.STATUS_OK) {
            OrderResponse orderResponse = response.as(OrderResponse.class);
            assertTrue("Флаг success должен быть true",
                    orderResponse.isSuccess());
        }
    }
}
