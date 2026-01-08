package utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

    private static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static Response get(String path) {
        return RestAssured.given()
                .spec(baseSpec())
                .when()
                .get(path);
    }

    public static Response post(String path, Object body) {
        return RestAssured.given()
                .spec(baseSpec())
                .body(body)
                .when()
                .post(path);
    }

    public static Response getWithAuth(String path, String accessToken) {
        return RestAssured.given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(path);
    }

    public static Response postWithAuth(String path, Object body, String accessToken) {
        return RestAssured.given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post(path);
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    public static String extractAccessToken(Response response) {
        return response.jsonPath().getString("accessToken");
    }

    public static Response patchWithAuth(String path, Object body, String accessToken) {
        return RestAssured.given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .patch(path);
    }

    public static Response deleteWithAuth(String path, String accessToken) {
        return RestAssured.given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(path);
    }

    public static Response deleteWithAuth(String path, Object body, String accessToken) {
        return RestAssured.given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .delete(path);
    }
}
