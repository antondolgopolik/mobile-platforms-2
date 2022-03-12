package by.bsuir.mobileplatforms2.service;

import com.android.volley.Response;

import java.util.function.Consumer;

import by.bsuir.mobileplatforms2.entity.Product;

public interface ProductService {

    void requestAllCategoryProducts(String category,
                                    Consumer<Product[]> listener,
                                    Response.ErrorListener errorListener);

    void requestProduct(int productId,
                        Consumer<Product> listener,
                        Response.ErrorListener errorListener);
}
