package by.bsuir.mobileplatforms2.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import by.bsuir.mobileplatforms2.ApplicationState;
import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.Product;
import by.bsuir.mobileplatforms2.service.FavoriteService;
import by.bsuir.mobileplatforms2.service.ImageLoaderService;
import by.bsuir.mobileplatforms2.service.impl.CachingImageLoaderService;
import by.bsuir.mobileplatforms2.service.impl.FavoriteServiceImpl;
import by.bsuir.mobileplatforms2.utils.PriceUtils;

public class ProductDetailsFragment extends Fragment {
    private Product product;

    private ImageLoaderService cachedImageService;
    private FavoriteService favoriteService;

    private Button addToFavoriteButton;
    private EditText commentEditText;

    private boolean isAddedToFavorite;

    public ProductDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Прочитать аргуметы
        Bundle arguments = getArguments();
        product = (Product) arguments.getSerializable("product");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Инициализация
        cachedImageService = new CachingImageLoaderService(getContext());
        favoriteService = new FavoriteServiceImpl(getContext());
        addToFavoriteButton = view.findViewById(R.id.productDetails_button_favorite);
        commentEditText = view.findViewById(R.id.productDetails_textEdit_favorite);
        // Начать фоновую загрузку ресурсов
        Future<Bitmap> productImage = cachedImageService.loadImage(product.getImage());
        // Настройка UI
        setupUi(view);
        // Установка слушателей
        addToFavoriteButton.setOnClickListener(this::addToFavoriteButtonOnClickListener);
        // После подгрузки ресурсов
        setProductImage(view, productImage);
    }

    private void setupUi(View view) {
        TextView titleTextView = view.findViewById(R.id.productDetails_textView_title);
        titleTextView.setText(product.getTitle());
        TextView priceTextView = view.findViewById(R.id.productDetails_textView_price);
        priceTextView.setText(
                "Price: " + PriceUtils.convert(product.getPrice()) + " " +
                        ApplicationState.ExchangeRate.getActiveCurrency()
        );
        TextView descriptionTextView = view.findViewById(R.id.productDetails_textView_description);
        descriptionTextView.setText(product.getDescription());
        RatingBar ratingBar = view.findViewById(R.id.productDetails_ratingBar);
        ratingBar.setRating(product.getRating().getRate());
        // Избранное
        if (favoriteService.isAddedToFavorite(product.getId())) {
            addToFavoriteButton.setText("Delete from favorite");
            commentEditText.setVisibility(View.GONE);
            isAddedToFavorite = true;
        } else {
            addToFavoriteButton.setText("Add to favorite");
            commentEditText.setVisibility(View.VISIBLE);
            isAddedToFavorite = false;
        }
    }

    private void addToFavoriteButtonOnClickListener(View view) {
        if (isAddedToFavorite) {
            favoriteService.removeFromFavorite(product.getId());
            addToFavoriteButton.setText("Add to favorite");
            commentEditText.setVisibility(View.VISIBLE);
            isAddedToFavorite = false;
        } else {
            favoriteService.addToFavorite(product.getId(), commentEditText.getText().toString());
            addToFavoriteButton.setText("Delete from favorite");
            commentEditText.setVisibility(View.GONE);
            isAddedToFavorite = true;
        }
    }

    private void setProductImage(View view, Future<Bitmap> productImage) {
        ImageView imageView = view.findViewById(R.id.productDetails_imageView);
        try {
            Bitmap bitmap = productImage.get();
            // Проверка наличия изображения
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getContext(), "Failed to load product image", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}