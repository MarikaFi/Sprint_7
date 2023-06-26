package model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
public class CourierRandomizer {
    static Faker faker = new Faker();

    public static final String NEW_LOGIN_FAKED = faker.name().username();
    @Step("Создание нового курьера с рандомными данными")
    public Courier createNewRandomCourier() {
        return new Courier(
                faker.name().username(),
                faker.internet().password(),
                faker.name().firstName());
    }
}
