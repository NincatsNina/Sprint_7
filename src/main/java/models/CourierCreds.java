package models;

public class CourierCreds {
    private String login;
    private String password;

    private CourierCreds(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCreds fromCourier(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    // Фабричный метод для негативных тестов
    public static CourierCreds from(String login, String password) {
        return new CourierCreds(login, password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
