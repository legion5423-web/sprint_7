package courier;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import models.Courier;

public class CourierCreateTest extends scr.test.java.BaseTest {

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        Courier courier = new Courier(login, password, firstName);
        courierApi.checkCreateSuccess(courierApi.createCourier(courier));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров должно возвращать ошибку")
    public void testCreateDuplicateCourier() {
        // Создаем первого курьера
        Courier courier = new Courier(login, password, firstName);
        courierApi.createCourier(courier);

        // Пытаемся создать второго с такими же данными
        courierApi.checkDuplicateError(courierApi.createCourier(courier));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void testCreateCourierWithoutLogin() {
        Courier courier = new Courier("", password, firstName);
        courierApi.checkMissingDataError(courierApi.createCourier(courier));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        Courier courier = new Courier(login, "", firstName);
        courierApi.checkMissingDataError(courierApi.createCourier(courier));
    }

    @Test
    @DisplayName("Создание курьера без имени (необязательное поле)")
    public void testCreateCourierWithoutFirstName() {
        Courier courier = new Courier(login, password, "");
        courierApi.checkCreateSuccess(courierApi.createCourier(courier));
    }
}