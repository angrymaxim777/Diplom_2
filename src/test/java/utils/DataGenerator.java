package utils;

import dto.request.CreateUserRequest;
import dto.request.LoginRequest;
import dto.request.OrderRequest;
import dto.response.IngredientsResponse;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final Random random = new Random();

    public static String generateUniqueEmail() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return "user_" + timestamp + "@test.ru";
    }

    public static String generatePassword() {
        return "Password" + random.nextInt(1000000);
    }

    public static String generateName() {
        String[] names = {"Иван", "Мария", "Петр", "Анна", "Сергей", "Ольга", "Алексей", "Елена"};
        return names[random.nextInt(names.length)] + "_" + random.nextInt(1000);
    }

    public static CreateUserRequest generateRandomUser() {
        return new CreateUserRequest(
                generateUniqueEmail(),
                generatePassword(),
                generateName()
        );
    }

    public static LoginRequest generateLoginCredentials(String email, String password) {
        return new LoginRequest(email, password);
    }

    public static OrderRequest generateValidOrder() {
        Response response = api.IngredientsApi.getIngredients();
        IngredientsResponse ingredientsResponse = response.as(IngredientsResponse.class);

        List<String> ingredientIds = new ArrayList<>();
        if (ingredientsResponse.isSuccess() && ingredientsResponse.getData() != null) {
            int count = Math.min(2, ingredientsResponse.getData().size());
            for (int i = 0; i < count; i++) {
                ingredientIds.add(ingredientsResponse.getData().get(i).getId());
            }
        }

        return new OrderRequest(ingredientIds);
    }

    public static OrderRequest generateOrderWithoutIngredients() {
        return new OrderRequest(new ArrayList<>());
    }

    public static OrderRequest generateOrderWithInvalidHash() {
        List<String> invalidHashes = new ArrayList<>();
        invalidHashes.add("invalid_hash_123");
        invalidHashes.add("another_invalid_456");
        return new OrderRequest(invalidHashes);
    }

    public static String generateInvalidEmail() {
        return "invalidemail";
    }

    public static String generateShortPassword() {
        return "123";
    }

    public static CreateUserRequest generateUserWithoutField(String missingField) {
        CreateUserRequest user = generateRandomUser();

        switch (missingField.toLowerCase()) {
            case "email":
                user.setEmail(null);
                break;
            case "password":
                user.setPassword(null);
                break;
            case "name":
                user.setName(null);
                break;
        }

        return user;
    }
}
