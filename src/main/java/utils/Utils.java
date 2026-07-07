import net.datafaker.Faker;

public class Utils {
    private static final Faker FAKER = new Faker();

    public static String randomLogin() {
        return FAKER.name().username();
    }

    public static String randomPassword() {
        return FAKER.internet().password(6, 12);
    }

    public static String randomFirstName() {
        return FAKER.name().firstName();
    }

    public static String randomLastName() {
        return FAKER.name().lastName();
    }

    public static String randomAddress() {
        return FAKER.address().streetAddress();
    }

    public static String randomPhone() {
        return FAKER.phoneNumber().cellPhone();
    }

    public static String randomComment() {
        return FAKER.lorem().sentence();
    }
}