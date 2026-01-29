package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private final RequestSpecification requestSpec;

    public OrderApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Создание заказа")
    public Response createOrder(models.Order order) {
        return given()
                .spec(requestSpec)
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .spec(requestSpec)
                .when()
                .get("/api/v1/orders");
    }

    @Step("Получение списка заказов с параметрами")
    public Response getOrdersWithParams(Integer courierId, String nearestStation, Integer limit, Integer page) {
        return given()
                .spec(requestSpec)
                .queryParam("courierId", courierId)
                .queryParam("nearestStation", nearestStation)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .when()
                .get("/api/v1/orders");
    }

    @Step("Получение заказа по трек-номеру")
    public Response getOrderByTrack(Integer track) {
        return given()
                .spec(requestSpec)
                .queryParam("t", track)
                .when()
                .get("/api/v1/orders/track");
    }

    @Step("Отмена заказа по трек-номеру")
    public Response cancelOrderByTrack(Integer track) {
        return given()
                .spec(requestSpec)
                .queryParam("track", track)
                .when()
                .put("/api/v1/orders/cancel");
    }

    @Step("Проверка успешного создания заказа")
    public void checkCreateSuccess(Response response) {
        response.then()
                .statusCode(201)
                .body("track", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Проверка успешного получения списка заказов")
    public void checkGetOrdersSuccess(Response response) {
        response.then()
                .statusCode(200)
                .body("orders", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Проверка получения заказа по трек-номеру")
    public void checkGetOrderByTrackSuccess(Response response) {
        response.then()
                .statusCode(200)
                .body("order", org.hamcrest.Matchers.notNullValue());
    }

    @Step("Проверка успешной отмены заказа")
    public void checkCancelOrderSuccess(Response response) {
        response.then()
                .statusCode(200)
                .body("ok", org.hamcrest.Matchers.is(true));
    }

    @Step("Проверка списка заказов с лимитом")
    public void checkGetOrdersWithLimitSuccess(Response response, int expectedLimit) {
        response.then()
                .statusCode(200)
                .body("orders", org.hamcrest.Matchers.notNullValue())
                .body("pageInfo.limit", org.hamcrest.Matchers.equalTo(expectedLimit));
    }

    @Step("Проверка списка заказов с фильтром станций")
    public void checkGetOrdersWithStationFilterSuccess(Response response) {
        response.then()
                .statusCode(200)
                .body("orders", org.hamcrest.Matchers.notNullValue())
                .body("availableStations", org.hamcrest.Matchers.notNullValue());
    }
}