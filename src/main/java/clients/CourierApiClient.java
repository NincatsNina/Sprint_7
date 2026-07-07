package clients;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCreds;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class CourierApiClient {


        private static final String API_V1_COURIER = "/api/v1/courier";
        private static final String API_V1_COURIER_LOGIN = "/api/v1/courier/login";

        public CourierApiClient() {
            RestAssured.baseURI = "https://qa-scooter.education-services.ru/";
        }

        @Step("Создать курьера")
        public Response createCourier(Courier courier) {
            return given()
                    .filter(new AllureRestAssured())
                    .contentType(JSON)
                    .and()
                    .body(courier)
                    .when()
                    .post(API_V1_COURIER);
        }

        @Step("Авторизовать курьера")
        public Response loginCourier(CourierCreds creds) {
            return given()
                    .filter(new AllureRestAssured())
                    .contentType(JSON)
                    .and()
                    .body(creds)
                    .when()
                    .post(API_V1_COURIER_LOGIN);
        }

        @Step("Удалить курьера")
        public Response deleteCourier(String id) {
            return given()
                    .filter(new AllureRestAssured())
                    .contentType(JSON)
                    .when()
                    .delete(API_V1_COURIER + "/" + id);
        }
    }

