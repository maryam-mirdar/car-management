package org.mirdar.api.validation;

public class CarLicenseValidation {

    public static boolean isValidationLicensePlate(String licensePlate) {
        if (licensePlate.length() != 8) {
            return false;
        }
        String regex = "^[0-9]{2}[A-Za-z]{1}[0-9]{5}$";
        return licensePlate.matches(regex);
    }
}