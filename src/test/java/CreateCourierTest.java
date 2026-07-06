import clients.CourierApiClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCreateResponse;
import models.CourierLoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static generator.CourierGenerator.randomCourier;
import static models.CourierCreds.fromCourier;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AllureJunit5.class)
public class CreateCourierTest {
    private final CourierApiClient courierApiClient = new CourierApiClient();
    private String id;

    @AfterEach
    public void tearDown() {
        if (id != null) {
            courierApiClient.deleteCourier(id);
        }
    }

    @Test
    public void createCourierTest() {
        Courier courier = randomCourier();
        Response created = courierApiClient.createCourier(courier);

        assertEquals(SC_CREATED, created.statusCode(), "Статус-код некорректен");
        assertTrue(created.as(CourierCreateResponse.class).isOk(), "Поле ok должно быть true");

        Response login = courierApiClient.loginCourier(fromCourier(courier));
        assertEquals(SC_OK, login.statusCode(), "Статус-код некорректен");

        id = login.as(CourierLoginResponse.class).getId();
    }

    @Test
    public void cannotCreateDuplicateCourier() {
        Courier courier = randomCourier();
        courierApiClient.createCourier(courier);

        Response login = courierApiClient.loginCourier(fromCourier(courier));
        id = login.as(CourierLoginResponse.class).getId();

        Response duplicate = courierApiClient.createCourier(courier);
        assertEquals(SC_CONFLICT, duplicate.statusCode(), "Статус-код некорректен");
        duplicate.then().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = randomCourier().setLogin(null);
        Response response = courierApiClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = randomCourier().setPassword(null);
        Response response = courierApiClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
