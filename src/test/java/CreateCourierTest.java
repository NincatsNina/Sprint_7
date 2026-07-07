import clients.CourierApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCreateResponse;
import models.CourierLoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Удаление тестовых данных")
    public void tearDown() {
        if (id != null) {
            courierApiClient.deleteCourier(id);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера со всеми обязательными полями")
    @Description("Проверяем, что при передаче валидного логина, пароля и имени курьер успешно регистрируется")
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
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    @Description("Проверяем, что при попытке зарегистрировать курьера с уже существующим логином возвращается ошибка 409")
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
    @DisplayName("Невозможно создать курьера без логина")
    @Description("Проверяем, что запрос на создание курьера без поля login отклоняется со статус-кодом 400")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = randomCourier().setLogin(null);
        Response response = courierApiClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Невозможно создать курьера без пароля")
    @Description("Проверяем, что запрос на создание курьера без поля password отклоняется со статус-кодом 400")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = randomCourier().setPassword(null);
        Response response = courierApiClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, response.statusCode(), "Статус-код некорректен");
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
