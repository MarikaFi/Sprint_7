package courier;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.*;
import client.*;

public class CourierCreationTest {
    protected final CourierRandomizer courierRandomizer = new CourierRandomizer();
    int courierId;
    private CourierClient courierClient;
    private Courier courier;
    private CourierAssert courierAssert;

    @Before
    @Step("Создание тестовых данных курьера")
    public void setUp() {
        courierClient = new CourierClient();
        courier = courierRandomizer.createNewRandomCourier();
        courierAssert = new CourierAssert();
    }

    @After
    @Step("Удаление тестовых данных")
    public void deleteCourier() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void courierCanBeCreated() {
        ValidatableResponse responseCreateCourier = courierClient.createCourier(courier);
        CourierCreds courierCreds = CourierCreds.from(courier);
        courierId = courierClient.loginCourier(courierCreds).extract().path("id");
        courierAssert.createCourierOk(responseCreateCourier);
    }

    @Test
    @DisplayName("Создание курьера с пустым полем логина")
    public void courierCanNotBeCreatedWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse responseNullLogin = courierClient.createCourier(courier);
        courierAssert.createCourierError(responseNullLogin);
    }

    @Test
    @DisplayName("Создание курьера с пустым полем пароля")
    public void courierCanNotBeCreatedWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse responseNullPassword = courierClient.createCourier(courier);
        courierAssert.createCourierError(responseNullPassword);
    }

    @Test
    @DisplayName("Создание курьера с пустым полем логина и пароля")
    public void courierCanNotBeCreatedWithoutLoginAndPassword() {
        courier.setLogin(null);
        courier.setPassword(null);
        ValidatableResponse responseNullFields = courierClient.createCourier(courier);
        courierAssert.createCourierError(responseNullFields);
    }

    @Test
    @DisplayName("Создание курьера с ранее зарегистрированным логином")
    public void courierCanNotBeCreatedWithSameLogin() {
        courierClient.createCourier(courier);
        ValidatableResponse responseCreateCourier = courierClient.createCourier(courier);
        courierAssert.createCourierSameLoginError(responseCreateCourier);
    }

}