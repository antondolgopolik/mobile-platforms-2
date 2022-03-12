package by.bsuir.mobileplatforms2;

import android.net.Uri;

import java.util.Map;

public class ApplicationState {

    private ApplicationState() {
    }

    public static class UserProfile {
        private static String username;
        private static String firstName;
        private static String lastName;
        private static Uri picture;

        private UserProfile() {
        }

        public static String getUsername() {
            return username;
        }

        public static void setUsername(String username) {
            UserProfile.username = username;
        }

        public static String getFirstName() {
            return firstName;
        }

        public static void setFirstName(String firstName) {
            UserProfile.firstName = firstName;
        }

        public static String getLastName() {
            return lastName;
        }

        public static void setLastName(String lastName) {
            UserProfile.lastName = lastName;
        }

        public static Uri getPicture() {
            return picture;
        }

        public static void setPicture(Uri picture) {
            UserProfile.picture = picture;
        }
    }

    public static class ExchangeRate {
        private static String activeCurrency = "BYN";
        private static Map<String, Double> exchangeRates;

        public static String getActiveCurrency() {
            return activeCurrency;
        }

        public static void setActiveCurrency(String activeCurrency) {
            ExchangeRate.activeCurrency = activeCurrency;
        }

        public static Map<String, Double> getExchangeRates() {
            return exchangeRates;
        }

        public static void setExchangeRates(Map<String, Double> exchangeRates) {
            ExchangeRate.exchangeRates = exchangeRates;
        }
    }
}
