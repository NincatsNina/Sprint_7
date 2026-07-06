import clients.OrderApiClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import models.Order;
import models.OrderCreateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AllureJunit5.class)
public class CreateOrderTest {
    private final OrderApiClient orderApiClient = new OrderApiClient();

    static Stream<List<String>> colorProvider() {
        return Stream.of(
                List.of("BLACK"),
                List.of("GREY"),
                List.of("BLACK", "GREY"),
                Collections.emptyList()
        );
    }

    @ParameterizedTest
    @MethodSource("colorProvider")
    public void createOrderWithVariousColors(List<String> colors) {
        Order order = new Order()
                .setFirstName("Alex")
                .setLastName("Ivanov")
                .setAddress("Moscow, Parkovaya, 10")
                .setMetroStation("4")
                .setPhone("+7 495 555 35 35")
                .setRentTime(5)
                .setDeliveryDate("2026-07-07")
                .setComment("Comment text")
                .setColor(colors);

        Response response = orderApiClient.createOrder(order);
        assertEquals(SC_CREATED, response.statusCode(), "Статус-код некорректен");

        String track = response.as(OrderCreateResponse.class).getTrack();
        assertNotNull(track, "Номер трека заказа не должен быть null");
    }

    @Test
    public void getOrderListReturnsOrders() {
        Response response = orderApiClient.getOrderList();
        assertEquals(SC_OK, response.statusCode(), "Статус-код некорректен");
        response.then().body("orders", notNullValue());
    }
}
