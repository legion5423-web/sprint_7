package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import models.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreateTest extends scr.test.java.BaseTest {

    private final List<String> colors;
    private Integer createdOrderTrack;

    public OrderCreateTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> getColors() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList()} // Пустой список
        });
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цветов")
    public void testCreateOrderWithDifferentColors() {
        // Создаем объект заказа
        Order order = new Order(
                "Иван",
                "Петров",
                "Москва, ул. Ленина, 1",
                "4",  // Сокольники
                "+79991234567",
                3,
                "2026-01-30",
                "Комментарий к заказу",
                colors
        );

        // Создаем заказ через OrderApi
        Response createResponse = orderApi.createOrder(order);

        // Проверяем успешное создание
        orderApi.checkCreateSuccess(createResponse);

        // Сохраняем трек-номер
        createdOrderTrack = createResponse.path("track");

        // Добавляем трек-номер в список для очистки
        addOrderTrackForCleanup(createdOrderTrack);

        // Проверяем, что заказ доступен по трек-номеру
        Response trackResponse = orderApi.getOrderByTrack(createdOrderTrack);
        orderApi.checkGetOrderByTrackSuccess(trackResponse);
    }

    @After
    public void cancelOrder() {
        // Отменяем созданный заказ после теста
        if (createdOrderTrack != null) {
            try {
                Response cancelResponse = orderApi.cancelOrderByTrack(createdOrderTrack);
                orderApi.checkCancelOrderSuccess(cancelResponse);
            } catch (Exception e) {
                System.out.println("Ошибка при отмене заказа " + createdOrderTrack + ": " + e.getMessage());
            }
        }
    }
}