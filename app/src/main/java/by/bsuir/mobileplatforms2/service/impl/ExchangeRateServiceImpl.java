package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;

import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.function.Consumer;

import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.dto.AllExchangeRatesResponse;
import by.bsuir.mobileplatforms2.service.ApiService;
import by.bsuir.mobileplatforms2.service.ExchangeRateService;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final Context context;
    private final ApiService apiService;

    public ExchangeRateServiceImpl(Context context) {
        this.context = context;
        apiService = new ApiServiceImpl(context);
    }

    @Override
    public void requestAllExchangeRates(String currency,
                                        Consumer<Map<String, Double>> listener,
                                        Response.ErrorListener errorListener) {
        apiService.sendGetRequest(
                allExchangeRatesUrl(currency),
                response -> allExchangeRatesListener(response, listener), errorListener
        );
    }

    private String allExchangeRatesUrl(String currency) {
        return context.getString(R.string.all_exchange_rates_url) + currency;
    }

    private void allExchangeRatesListener(String response, Consumer<Map<String, Double>> listener) {
        try {
            AllExchangeRatesResponse allExchangeRatesResponse =
                    new ObjectMapper().readerFor(AllExchangeRatesResponse.class).readValue(response);
            listener.accept(allExchangeRatesResponse.getConversionRates());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
