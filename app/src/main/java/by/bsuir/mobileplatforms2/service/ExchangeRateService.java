package by.bsuir.mobileplatforms2.service;

import com.android.volley.Response;

import java.util.Map;
import java.util.function.Consumer;

public interface ExchangeRateService {

    void requestAllExchangeRates(String currency,
                                 Consumer<Map<String, Double>> listener,
                                 Response.ErrorListener errorListener);
}
