package models;

import java.util.List;

public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public List<String> getColor() {
        return color;
    }

    public Order setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Order setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public Order setMetroStation(String metroStation) {
        this.metroStation = metroStation;
        return this;
    }

    public Order setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Order setRentTime(int rentTime) {
        this.rentTime = rentTime;
        return this;
    }

    public Order setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public Order setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Order setColor(List<String> color) {
        this.color = color;
        return this;
    }
}
