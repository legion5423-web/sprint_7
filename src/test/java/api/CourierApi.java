package api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Courier;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private final RequestSpecification requestSpec;

    public CourierApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .spec(requestSpec)
                .body(courier)  // Автоматическая сериализация в JSON
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public Response loginCourier(String login, String password) {
        Courier loginData = new Courier();
        loginData.setLogin(login);
        loginData.setPassword(password);

        return given()
                .spec(requestSpec)
                .body(loginData)  // Автоматическая сериализация
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Получение ID курьера")
    public Integer getCourierId(String login, String password) {
        Response response = loginCourier(login, password);
        if (response.statusCode() == 200) {
            return response.path("id");
        }
        return null;
    }

    @Step("Удаление курьера с ID: {courierId}")
    public Response deleteCourier(Integer courierId) {
        if (courierId == null) {
            return null;
        }

        return given()
                .spec(requestSpec)
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

    @Step("Проверка успешного создания курьера")
    public void checkCreateSuccess(Response response) {
        response.then()
                .statusCode(201)
                .body("ok", org.hamcrest.Matchers.is(true));
    }

    @Step("Проверка ошибки дублирования курьера")
    public void checkDuplicateError(Response response) {
        response.then()
                .statusCode(409)
                .body("message", org.hamcrest.Matchers.equalTo("Этот логин уже используется"));
    }

    @Step("Проверка ошибки недостатка данных")
    public void checkMissingDataError(Response response) {
        response.then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }
}