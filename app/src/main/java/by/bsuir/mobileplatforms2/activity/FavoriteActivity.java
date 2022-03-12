package by.bsuir.mobileplatforms2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.Favorite;
import by.bsuir.mobileplatforms2.entity.Product;
import by.bsuir.mobileplatforms2.service.FavoriteService;
import by.bsuir.mobileplatforms2.service.ProductService;
import by.bsuir.mobileplatforms2.service.impl.FavoriteServiceImpl;
import by.bsuir.mobileplatforms2.service.impl.ProductServiceImpl;

public class FavoriteActivity extends AppCompatActivity {
    private FavoriteService favoriteService;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        // Инициализация
        favoriteService = new FavoriteServiceImpl(this);
        productService = new ProductServiceImpl(this);
        // Отображение
        LinearLayout linearLayout = findViewById(R.id.favorite_linearLayout);
        List<Favorite> favorites = favoriteService.getAllFavorites();
        for (Favorite favorite : favorites) {
            View view = getLayoutInflater().inflate(R.layout.item_favorite_list, null);
            Button button = view.findViewById(R.id.item_favorite_list_button);
            TextView textView = view.findViewById(R.id.item_favorite_list_comment);
            // Установка содержимого
            productService.requestProduct(favorite.getProductId(),
                    product -> {
                        button.setText(product.getTitle());
                        button.setOnClickListener(view1 -> {
                            Intent intent = new Intent(FavoriteActivity.this, WebViewActivity.class);
                            intent.putExtra("productURL", product.getImage());
                            startActivity(intent);
                        });
                    },
                    error -> {
                        throw new RuntimeException("Failed to load product");
                    });
            textView.setText(favorite.getComment());
            // Добавление
            linearLayout.addView(view);
        }
    }
}