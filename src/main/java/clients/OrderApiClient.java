package clients;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class OrderApiClient {
    private static final String API_V1_ORDERS = "/api/v1/orders";

    public OrderApiClient() {
        RestAssured.baseURI = "https://qa-scooter.education-services.ru/";
    }

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .and()
                .body(order)
                .when()
                .post(API_V1_ORDERS);
    }

    @Step("Получить список заказов")
    public Response getOrderList() {
        return given()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .when()
                .get(API_V1_ORDERS);
    }
}
