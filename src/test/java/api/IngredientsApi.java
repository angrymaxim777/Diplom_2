package api;

import io.restassured.response.Response;
import utils.ApiClient;
import utils.TestConfig;

public class IngredientsApi {

    public static Response getIngredients() {
        return ApiClient.get(TestConfig.INGREDIENTS_PATH);
    }

    public static String[] getValidIngredientIds() {
        Response response = getIngredients();
        return response.jsonPath().getString("data.id").split(",");
    }

    public static String getInvalidIngredientId() {
        return "invalid_id_12345";
    }
}
