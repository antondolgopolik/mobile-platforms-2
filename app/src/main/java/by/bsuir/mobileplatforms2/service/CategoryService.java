package by.bsuir.mobileplatforms2.service;

import com.android.volley.Response;

import java.util.function.Consumer;

public interface CategoryService {

    void requestAllCategoryNames(Consumer<String[]> listener,
                                 Response.ErrorListener errorListener);
}
