package api;

import dto.request.OrderRequest;
import io.restassured.response.Response;
import utils.ApiClient;
import utils.TestConfig;

import java.util.List;

public class OrderApi {

    public static Response createOrder(OrderRequest orderRequest) {
        return ApiClient.post(TestConfig.ORDERS_PATH, orderRequest);
    }

    public static Response createOrder(List<String> ingredients) {
        OrderRequest orderRequest = new OrderRequest(ingredients);
        return createOrder(orderRequest);
    }

    public static Response createOrderWithAuth(OrderRequest orderRequest, String accessToken) {
        return ApiClient.postWithAuth(TestConfig.ORDERS_PATH, orderRequest, accessToken);
    }

    public static Response createOrderWithAuth(List<String> ingredients, String accessToken) {
        OrderRequest orderRequest = new OrderRequest(ingredients);
        return createOrderWithAuth(orderRequest, accessToken);
    }

    public static Response getUserOrders(String accessToken) {
        return ApiClient.getWithAuth(TestConfig.ORDERS_PATH, accessToken);
    }
}
