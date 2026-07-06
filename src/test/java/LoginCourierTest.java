import clients.CourierApiClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import models.Courier;
import models.CourierLoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static generator.CourierGenerator.randomCourier;
import static models.CourierCreds.*;
import static models.CourierCreds.fromCourier;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AllureJunit5.class)
public class LoginCourierTest {
    private final CourierApiClient courierApiClient = new CourierApiClient();
    private Courier courier;
    private String id;

    @BeforeEach
    public void setUp() {
        courier = randomCourier();
        courierApiClient.createCourier(courier);
    }

    @AfterEach
    public void tearDown() {
        if (id != null) {
            courierApiClient.deleteCourier(id);
        } else {
            try {
                Response login = courierApiClient.loginCourier(fromCourier(courier));
                id = login.as(CourierLoginResponse.class).getId();
                courierApiClient.deleteCourier(id);
            } catch (Exception ignored) {}
        }
    }

    @Test
    public void courierCanLoginSuccessfully() {
        Response response = courierApiClient.loginCourier(fromCourier(courier));

        assertEquals(SC_OK, response.statusCode(), "Статус-код некорректен");
        id = response.as(CourierLoginResponse.class).getId();
        assertNotNull(id, "ID не должно быть null");
    }

    @Test
    public void loginWithIncorrectPasswordReturnsError() {
        Response response = courierApiClient.loginCourier(from(courier.getLogin(), "wrong_pass"));

        assertEquals(SC_NOT_FOUND, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginWithoutUsernameReturnsError() {
        Response response = courierApiClient.loginCourier(from("", courier.getPassword()));

        assertEquals(SC_BAD_REQUEST, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginNonExistentCourierReturnsError() {
        Response response = courierApiClient.loginCourier(from("non_existent_user_999", "some_pass"));

        assertEquals(SC_NOT_FOUND, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }
}
