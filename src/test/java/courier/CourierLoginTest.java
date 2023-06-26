package courier;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.*;
import client.*;

public class CourierLoginTest {
    private final CourierRandomizer courierRandomizer = new CourierRandomizer();
    private int courierID;
    private CourierAssert courierAssert;
    private CourierCreds courierCreds;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    @Step("Создание тестовых данных для логина курьера")
    public void setUp() {
        courierClient = new CourierClient();
        courier = courierRandomizer.createNewRandomCourier();
        courierClient.createCourier(courier);
        courierCreds = CourierCreds.from(courier);
        courierAssert = new CourierAssert();
    }

    @Test
    @DisplayName("Логин курьера успешен")
    public void courierLoginOkValidData() {
        ValidatableResponse responseLoginCourier = courierClient.loginCourier(courierCreds);
        courierAssert.loginCourierOk(responseLoginCourier);
        courierID = responseLoginCourier.extract().path("id");
    }

    @Test
    @DisplayName("Логин курьера с пустым полем логина")
    public void courierLoginErrorEmptyLogin() {
        CourierCreds courierCredsWithoutLogin = new CourierCreds("", courier.getPassword()); // c null тесты виснут
        ValidatableResponse responseLoginErrorMessage = courierClient.loginCourier(courierCredsWithoutLogin);
        courierAssert.loginCourierError(responseLoginErrorMessage);

    }

    @Test
    @DisplayName("Логин курьера с пустым полем пароля")
    public void courierLoginErrorEmptyPassword() {
        CourierCreds courierCredsWithoutPass = new CourierCreds(courier.getLogin(), "");
        ValidatableResponse responseLoginErrorMessage = courierClient.loginCourier(courierCredsWithoutPass);
        courierAssert.loginCourierError(responseLoginErrorMessage);
    }

    @Test
    @DisplayName("Логин курьера с пустым полями логина и пароля")
    public void courierLoginErrorEmptyLoginAndPassword() {
        CourierCreds courierCredsWithoutLoginAndPassword = new CourierCreds("", "");
        ValidatableResponse responseLoginErrorMessage = courierClient.loginCourier(courierCredsWithoutLoginAndPassword);
        courierAssert.loginCourierError(responseLoginErrorMessage);
    }

    @Test
    @DisplayName("Логин курьера c невалидным логином")
    public void courierLoginErrorAccountNotFound() {
        CourierCreds courierCredsErrorAccountNotFound = new CourierCreds(CourierRandomizer.NEW_LOGIN_FAKED, courier.getPassword());
        ValidatableResponse responseLoginErrorMessage = courierClient.loginCourier(courierCredsErrorAccountNotFound);
        courierAssert.loginCourierErrorAccountNotFound(responseLoginErrorMessage);
    }

    @After
    @Step("Удаление курьера")
    public void deleteCourier() {
        if (courierID != 0) {
            courierClient.deleteCourier(courierID);
        }
    }
}
