package by.bsuir.mobileplatforms2.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import by.bsuir.mobileplatforms2.ApplicationState;
import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.Product;
import by.bsuir.mobileplatforms2.service.CategoryService;
import by.bsuir.mobileplatforms2.service.HistoryService;
import by.bsuir.mobileplatforms2.service.ProductService;
import by.bsuir.mobileplatforms2.service.impl.CategoryServiceImpl;
import by.bsuir.mobileplatforms2.service.impl.HistoryServiceImpl;
import by.bsuir.mobileplatforms2.service.impl.ProductServiceImpl;
import by.bsuir.mobileplatforms2.utils.ConnectionUtils;
import by.bsuir.mobileplatforms2.utils.PriceUtils;

public class SearchFragment extends Fragment {
    private Spinner spinner;
    private EditText editText;
    private Button searchButton;

    private CategoryService categoryService;
    private ProductService productService;
    private HistoryService historyService;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Инициализация
        categoryService = new CategoryServiceImpl(getContext());
        productService = new ProductServiceImpl(getContext());
        historyService = new HistoryServiceImpl(getContext());
        spinner = view.findViewById(R.id.search_spinner);
        editText = view.findViewById(R.id.search_price_edit);
        searchButton = view.findViewById(R.id.search_button);
        // Начать фоновую загрузку ресурсов
        categoryService.requestAllCategoryNames(
                this::allCategoryNamesListener, this::allCategoryNamesErrorListener
        );
        // Установить слушатели
        searchButton.setOnClickListener(this::searchButtonOnClickListener);
    }

    private void allCategoryNamesListener(String[] categoryNames) {
        spinner = getView().findViewById(R.id.search_spinner);
        spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames));
    }

    private void allCategoryNamesErrorListener(VolleyError error) {
        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }

    private void searchButtonOnClickListener(View view) {
        // Проверка соединения
        if (ConnectionUtils.isOnline(getContext())) {
            // Сохранить историю
            saveHistory();
            // Выполнить поиск
            String categoryName = (String) spinner.getSelectedItem();
            productService.requestAllCategoryProducts(
                    categoryName, this::allCategoryProductsListener, this::allCategoryProductsErrorListener
            );
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void saveHistory() {
        String username = ApplicationState.UserProfile.getUsername();
        String categoryName = (String) spinner.getSelectedItem();
        String strPrice = editText.getText().toString();
        Double price = strPrice.isEmpty() ? null : Double.parseDouble(strPrice);
        historyService.saveInHistory(username, categoryName, price);
    }

    private void allCategoryProductsListener(Product[] products) {
        // Фильтровка продуктов
        Product[] filtered = filterProducts(products);
        // Подготовка аргументов
        Bundle args = new Bundle();
        args.putSerializable("products", filtered);
        // Переход
        NavHostFragment.findNavController(this).navigate(
                R.id.action_searchFragment_to_productListFragment, args
        );
    }

    private Product[] filterProducts(Product[] products) {
        String strUpperBound = editText.getText().toString();
        if (!strUpperBound.isEmpty()) {
            double upperBound = Double.parseDouble(strUpperBound);
            return Arrays.stream(products)
                    .filter(product -> PriceUtils.convert(product.getPrice()) <= upperBound)
                    .toArray(Product[]::new);
        } else {
            return products;
        }
    }

    private void allCategoryProductsErrorListener(VolleyError error) {
        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }
}