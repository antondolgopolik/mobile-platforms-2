package by.bsuir.mobileplatforms2.utils;

import by.bsuir.mobileplatforms2.ApplicationState;

public class PriceUtils {

    public static double convert(double price) {
        return price * ApplicationState.ExchangeRate.getExchangeRates()
                .get(ApplicationState.ExchangeRate.getActiveCurrency());
    }
}
