package helpers;

public class CourierGenerator {

    public static String generateRandomLogin() {
        return "courier_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public static String generateRandomPassword() {
        return "pass_" + System.currentTimeMillis();
    }

    public static String generateRandomFirstName() {
        return "Тест_" + System.currentTimeMillis();
    }
}