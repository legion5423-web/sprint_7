package courier;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.Before;
import models.Courier;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends scr.test.java.BaseTest {

    @Before
    public void setUpCourier() {
        // Создаем курьера перед тестами логина
        Courier courier = new Courier(login, password, firstName);
        courierApi.createCourier(courier);
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void testCourierLoginSuccess() {
        courierApi.loginCourier(login, password)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void testCourierLoginWithoutPassword() {
        courierApi.loginCourier(login, "")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void testCourierLoginWithoutLogin() {
        courierApi.loginCourier("", password)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void testCourierLoginWrongPassword() {
        courierApi.loginCourier(login, "wrongPassword")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин под несуществующим пользователем")
    public void testCourierLoginNonExistentUser() {
        courierApi.loginCourier("nonExistentUser", password)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}