package courier;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends src.test.java.BaseTest {

    @Before
    public void setUpCourier() {
        // Создаем курьера перед тестами логина
        createCourier(login, password, firstName);
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void testCourierLoginSuccess() {
        loginCourier(login, password)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void testCourierLoginWithoutPassword() {
        loginCourier(login, "")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void testCourierLoginWithoutLogin() {
        loginCourier("", password)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void testCourierLoginWrongPassword() {
        loginCourier(login, "wrongPassword")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин под несуществующим пользователем")
    public void testCourierLoginNonExistentUser() {
        loginCourier("nonExistentUser", password)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}