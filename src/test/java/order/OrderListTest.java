package order;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderListTest extends src.test.java.BaseTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrderList() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0))
                .body("pageInfo", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов с лимитом 10")
    public void testGetOrderListWithLimit() {
        given()
                .header("Content-type", "application/json")
                .queryParam("limit", 10)
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", lessThanOrEqualTo(10))
                .body("pageInfo.limit", equalTo(10));
    }

    @Test
    @DisplayName("Получение списка заказов с фильтром по станциям метро")
    public void testGetOrderListWithStationFilter() {
        given()
                .header("Content-type", "application/json")
                .queryParam("nearestStation", "[\"1\", \"2\"]")
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("availableStations", notNullValue());
    }
}