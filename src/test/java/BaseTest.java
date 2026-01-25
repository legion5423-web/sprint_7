package src.test.java;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    protected String login;
    protected String password;
    protected String firstName;
    protected Integer courierId;
    protected Integer orderTrack;

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

        // Настройка спецификации запросов с Allure фильтром
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())  // Важно для Allure отчетов
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    @Step("Создание курьера: {login}")
    protected Response createCourier(String login, String password, String firstName) {
        String body = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                login, password, firstName);

        return given()
                .spec(requestSpec)  // Используем общую спецификацию
                .body(body)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера: {login}")
    protected Response loginCourier(String login, String password) {
        String body = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);

        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Получение ID курьера")
    protected Integer getCourierId(String login, String password) {
        Response response = loginCourier(login, password);
        if (response.statusCode() == 200) {
            return response.path("id");
        }
        return null;
    }

    @Step("Удаление курьера с ID: {courierId}")
    protected Response deleteCourier(Integer courierId) {
        if (courierId == null) {
            return null;
        }

        return given()
                .spec(requestSpec)
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

    @Step("Создание заказа с цветами: {colors}")
    public Response createOrder(String firstName, String lastName, String address,
                                String metroStation, String phone, Integer rentTime,
                                String deliveryDate, String comment, String[] colors) {

        StringBuilder colorsJson = new StringBuilder("[");
        if (colors != null && colors.length > 0) {
            for (int i = 0; i < colors.length; i++) {
                colorsJson.append("\"").append(colors[i]).append("\"");
                if (i < colors.length - 1) {
                    colorsJson.append(",");
                }
            }
        }
        colorsJson.append("]");

        String body = String.format(
                "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"address\":\"%s\"," +
                        "\"metroStation\":\"%s\",\"phone\":\"%s\",\"rentTime\":%d," +
                        "\"deliveryDate\":\"%s\",\"comment\":\"%s\"," +
                        "\"color\":%s}",
                firstName, lastName, address, metroStation, phone, rentTime,
                deliveryDate, comment, colorsJson.toString());

        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/api/v1/orders");
    }

    @After
    public void tearDown() {
        // Удаление созданного курьера после теста
        if (login != null && password != null) {
            try {
                Integer id = getCourierId(login, password);
                if (id != null) {
                    deleteCourier(id);
                }
            } catch (Exception e) {

                System.out.println("Ошибка при удалении курьера: " + e.getMessage());
            }
        }
    }
}