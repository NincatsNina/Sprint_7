package generator;

import models.Courier;
import net.datafaker.Faker;

public class CourierGenerator {
    private static final Faker FAKER = new Faker();

    public static Courier randomCourier() {
        return new Courier()
                .setLogin(FAKER.name().username())
                .setPassword(FAKER.internet().password(6, 12))
                .setFirstName(FAKER.name().firstName());
    }
}
