package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;

import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Consumer;

import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.service.ApiService;
import by.bsuir.mobileplatforms2.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    private final Context context;
    private final ApiService apiService;

    public CategoryServiceImpl(Context context) {
        this.context = context;
        apiService = new ApiServiceImpl(context);
    }

    @Override
    public void requestAllCategoryNames(Consumer<String[]> listener,
                                        Response.ErrorListener errorListener) {
        apiService.sendGetRequest(
                context.getString(R.string.all_category_names_url),
                response -> allCategoryNamesListener(response, listener), errorListener
        );
    }

    private void allCategoryNamesListener(String response, Consumer<String[]> listener) {
        try {
            String[] categoryNames = new ObjectMapper().readerFor(String[].class).readValue(response);
            listener.accept(categoryNames);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
