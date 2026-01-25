package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreateTest extends src.test.java.BaseTest {

    private final String[] colors;

    public OrderCreateTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> getColors() {
        return Arrays.asList(new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цветов")
    public void testCreateOrderWithDifferentColors() {
        Response response = createOrder(
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

        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        orderTrack = response.path("track");

        // Проверяем, что заказ действительно создан через получение по треку
        given()
                .spec(requestSpec)
                .queryParam("t", orderTrack)
                .when()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(200)
                .body("order", notNullValue());
    }
}