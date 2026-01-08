package utils;

import dto.request.CreateUserRequest;
import dto.response.CreateUserResponse;
import dto.response.ErrorResponse;
import dto.response.IngredientsResponse;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {

    public static CreateUserResponse registerUser(CreateUserRequest userRequest) {
        Response response = api.AuthApi.register(userRequest);

        if (response.getStatusCode() == TestConfig.STATUS_OK) {
            return response.as(CreateUserResponse.class);
        } else {
            ErrorResponse error = response.as(ErrorResponse.class);
            throw new RuntimeException("Failed to register user: " + error.getMessage());
        }
    }

    public static String loginAndGetToken(String email, String password) {
        Response response = api.AuthApi.login(email, password);
        ApiClient.assertStatusCode(response, TestConfig.STATUS_OK);
        return ApiClient.extractAccessToken(response);
    }

    public static String createUserAndGetToken() {
        CreateUserRequest userRequest = DataGenerator.generateRandomUser();
        CreateUserResponse response = registerUser(userRequest);
        return response.getAccessToken();
    }

    public static void deleteUser(String accessToken) {
        if (accessToken != null) {
            Response response = api.AuthApi.deleteUser(accessToken);
            if (response.getStatusCode() == TestConfig.STATUS_OK) {
                System.out.println("Пользователь успешно удален");
            }
        }
    }

    public static List<String> getValidIngredientIds() {
        Response response = api.IngredientsApi.getIngredients();
        IngredientsResponse ingredientsResponse = response.as(IngredientsResponse.class);

        List<String> ingredientIds = new ArrayList<>();
        if (ingredientsResponse.isSuccess() && ingredientsResponse.getData() != null) {

            int count = Math.min(3, ingredientsResponse.getData().size());
            for (int i = 0; i < count; i++) {
                ingredientIds.add(ingredientsResponse.getData().get(i).getId());
            }
        }
        return ingredientIds;
    }
}
