package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Consumer;

import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.Product;
import by.bsuir.mobileplatforms2.service.ApiService;
import by.bsuir.mobileplatforms2.service.ProductService;

public class ProductServiceImpl implements ProductService {
    private final Context context;
    private final ApiService apiService;

    public ProductServiceImpl(Context context) {
        this.context = context;
        apiService = new ApiServiceImpl(context);
    }

    @Override
    public void requestAllCategoryProducts(String category,
                                           Consumer<Product[]> listener,
                                           Response.ErrorListener errorListener) {
        apiService.sendGetRequest(
                allCategoryProductsUrl(category),
                response -> allCategoryProductsListener(response, listener), errorListener
        );
    }

    private String allCategoryProductsUrl(String category) {
        return context.getString(R.string.all_category_products_url) + Uri.encode(category);
    }

    private void allCategoryProductsListener(String response, Consumer<Product[]> listener) {
        try {
            Product[] products = new ObjectMapper().readerFor(Product[].class).readValue(response);
            listener.accept(products);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestProduct(int productId,
                               Consumer<Product> listener,
                               Response.ErrorListener errorListener) {
        apiService.sendGetRequest(
                productsUrl(productId),
                response -> productListener(response, listener), errorListener
        );
    }

    private String productsUrl(int productId) {
        return context.getString(R.string.product_url) + productId;
    }

    private void productListener(String response, Consumer<Product> listener) {
        try {
            Product product = new ObjectMapper().readerFor(Product.class).readValue(response);
            listener.accept(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
