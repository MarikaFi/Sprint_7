package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import client.*;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {
    @Test
    @DisplayName("Получение списка заказов")
    public void getOrderList() {
        OrdersClient orderClient = new OrdersClient();
        ValidatableResponse responseOrderList = orderClient.getOrderList();
        responseOrderList.assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
