package by.bsuir.mobileplatforms2.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.Product;
import by.bsuir.mobileplatforms2.service.CategoryService;
import by.bsuir.mobileplatforms2.service.ProductService;
import by.bsuir.mobileplatforms2.service.impl.CategoryServiceImpl;
import by.bsuir.mobileplatforms2.service.impl.ProductServiceImpl;
import by.bsuir.mobileplatforms2.utils.ConnectionUtils;

public class CategoryListFragment extends Fragment {
    private LinearLayout linearLayout;

    private CategoryService categoryService;
    private ProductService productService;

    private Map<Integer, Integer> categoryButtonIdToIndex;
    private String[] categoryNames;

    public CategoryListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Инициализация
        categoryService = new CategoryServiceImpl(getContext());
        productService = new ProductServiceImpl(getContext());
        linearLayout = getView().findViewById(R.id.categoryList_linearLayout);
        // Начать фоновую загрузку ресурсов
        categoryService.requestAllCategoryNames(
                this::allCategoryNamesListener, this::allCategoryNamesErrorListener
        );
    }

    private void allCategoryNamesListener(String[] categoryNames) {
        categoryButtonIdToIndex = new HashMap<>();
        this.categoryNames = categoryNames;
        // Отобразить кнопки
        for (int i = 0; i < categoryNames.length; i++) {
            // Создание кнопки
            Button button = (Button) getLayoutInflater().inflate(R.layout.item_category_list, null);
            button.setText(categoryNames[i]);
            button.setOnClickListener(this::categoryButtonOnClickListener);
            // Установка id
            int id = View.generateViewId();
            button.setId(id);
            categoryButtonIdToIndex.put(id, i);
            // Отображение
            linearLayout.addView(button);
        }
    }

    private void allCategoryNamesErrorListener(VolleyError error) {
        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }

    private void categoryButtonOnClickListener(View view) {
        // Проверка соединения
        if (ConnectionUtils.isOnline(getContext())) {
            int index = categoryButtonIdToIndex.get(view.getId());
            // Запрос на получение продуктов
            productService.requestAllCategoryProducts(
                    categoryNames[index], this::allCategoryProductsListener, this::allCategoryProductsErrorListener
            );
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void allCategoryProductsListener(Product[] products) {
        // Подготовка аргументов
        Bundle args = new Bundle();
        args.putSerializable("products", products);
        // Переход
        NavHostFragment.findNavController(this).navigate(
                R.id.action_categoryListFragment_to_productListFragment, args
        );
    }

    private void allCategoryProductsErrorListener(VolleyError error) {
        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }
}