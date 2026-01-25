package courier;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.After;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest extends src.test.java.BaseTest {

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        createCourier(login, password, firstName)
                .then()
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров должно возвращать ошибку")
    public void testCreateDuplicateCourier() {
        // Создаем первого курьера
        createCourier(login, password, firstName);

        // Пытаемся создать второго с такими же данными
        createCourier(login, password, firstName)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void testCreateCourierWithoutLogin() {
        createCourier("", password, firstName)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        createCourier(login, "", firstName)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени (необязательное поле)")
    public void testCreateCourierWithoutFirstName() {
        createCourier(login, password, "")
                .then()
                .statusCode(201)  // Имя необязательное поле
                .body("ok", is(true));
    }
}