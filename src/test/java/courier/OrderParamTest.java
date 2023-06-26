package courier;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import model.Order;
import client.OrdersClient;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderParamTest {
    private final List<String> colour;
    private int track;
    private OrdersClient orderClient;

    public OrderParamTest(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getScooterColour() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK, GRAY")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrdersClient();
    }

    @After
    @Step("Cancel test order")
    public void CancelTestOrder() {
        orderClient.cancelOrder(track);
    }

    @Test
    @DisplayName("Размещение заказа с самокатами разных цветов")
    public void OrderingWithScootersInDifferentColors() {
        Order order = new Order(colour);
        ValidatableResponse responseCreateOrder = orderClient.createNewOrder(order);
        track = responseCreateOrder.extract().path("track");
        responseCreateOrder.assertThat()
                .statusCode(201)
                .body("track", is(notNullValue()));
    }
}
