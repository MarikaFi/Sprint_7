package client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.Order;

import static io.restassured.RestAssured.given;
import static constants.constant.*;

public class OrdersClient{
    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    @Step("Создание заказа")
    public ValidatableResponse createNewOrder(Order order) {
        return requestSpec()
                .body(order)
                .when()
                .post(ORDER_POST_CREATE)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return requestSpec()
                .when()
                .get(ORDER_GET_LIST)
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track) {
        return requestSpec()
                .body(track)
                .when()
                .put(ORDER_PUT_CANCEL)
                .then();
    }
}
