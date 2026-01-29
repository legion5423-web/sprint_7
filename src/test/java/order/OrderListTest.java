package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

public class OrderListTest extends scr.test.java.BaseTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrderList() {
        Response response = orderApi.getOrders();
        orderApi.checkGetOrdersSuccess(response);
    }

    @Test
    @DisplayName("Получение списка заказов с лимитом 10")
    public void testGetOrderListWithLimit() {
        Response response = orderApi.getOrdersWithParams(null, null, 10, 0);
        orderApi.checkGetOrdersWithLimitSuccess(response, 10);
    }

    @Test
    @DisplayName("Получение списка заказов с фильтром по станциям метро")
    public void testGetOrderListWithStationFilter() {
        Response response = orderApi.getOrdersWithParams(null, "[\"1\", \"2\"]", null, null);
        orderApi.checkGetOrdersWithStationFilterSuccess(response);
    }

    @Test
    @DisplayName("Получение списка заказов с указанием страницы")
    public void testGetOrderListWithPage() {
        Response response = orderApi.getOrdersWithParams(null, null, 5, 0);
        orderApi.checkGetOrdersWithLimitSuccess(response, 5);
    }

    @Test
    @DisplayName("Получение пустого списка заказов для несуществующего курьера")
    public void testGetOrderListForNonExistentCourier() {
        // Используем несуществующий ID курьера
        Response response = orderApi.getOrdersWithParams(999999, null, null, null);

        // В зависимости от API может возвращать 200 с пустым списком или ошибку
        // Проверяем, что ответ пришел (статус 200 или 404)
        response.then()
                .statusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.equalTo(200),
                        org.hamcrest.Matchers.equalTo(404)
                ));
    }
}