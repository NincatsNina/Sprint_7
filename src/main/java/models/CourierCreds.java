package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CourierCreds {
    private String login;
    private String password;

    public static CourierCreds fromCourier(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    public static CourierCreds from(String login, String password) {
        return new CourierCreds(login, password);
    }
}