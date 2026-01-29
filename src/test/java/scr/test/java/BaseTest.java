package scr.test.java;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import api.CourierApi;
import api.OrderApi;
import models.Courier;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    protected String login;
    protected String password;
    protected String firstName;
    protected Integer courierId;
    protected List<Integer> createdOrderTracks; // Список созданных заказов для очистки

    // API клиенты
    protected CourierApi courierApi;
    protected OrderApi orderApi;

    // Общая спецификация для всех запросов
    protected RequestSpecification requestSpec;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        // Генерация уникальных тестовых данных для каждого теста
        long timestamp = System.currentTimeMillis();
        login = "testCourier" + timestamp;
        password = "password123";
        firstName = "Тест" + timestamp;

        // Инициализация списка заказов
        createdOrderTracks = new ArrayList<>();

        // Настройка спецификации запросов
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        // Инициализация API клиентов
        courierApi = new CourierApi(requestSpec);
        orderApi = new OrderApi(requestSpec);
    }

    @After
    public void tearDown() {
        // Отмена всех созданных заказов
        cancelAllCreatedOrders();

        // Удаление созданного курьера после теста
        deleteCreatedCourier();
    }

    @io.qameta.allure.Step("Отмена всех созданных заказов")
    protected void cancelAllCreatedOrders() {
        for (Integer track : createdOrderTracks) {
            try {
                orderApi.cancelOrderByTrack(track);
            } catch (Exception e) {
                System.out.println("Не удалось отменить заказ " + track + ": " + e.getMessage());
            }
        }
        createdOrderTracks.clear();
    }

    @io.qameta.allure.Step("Удаление созданного курьера")
    protected void deleteCreatedCourier() {
        if (login != null && password != null) {
            try {
                Integer id = courierApi.getCourierId(login, password);
                if (id != null) {
                    courierApi.deleteCourier(id);
                }
            } catch (Exception e) {
                System.out.println("Ошибка при удалении курьера: " + e.getMessage());
            }
        }
    }

    @io.qameta.allure.Step("Добавление трек-номера в список для очистки")
    protected void addOrderTrackForCleanup(Integer track) {
        if (track != null) {
            createdOrderTracks.add(track);
        }
    }
}