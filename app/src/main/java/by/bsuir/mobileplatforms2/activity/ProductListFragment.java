package by.bsuir.mobileplatforms2.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import by.bsuir.mobileplatforms2.ApplicationState;
import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.Product;
import by.bsuir.mobileplatforms2.service.ProductService;
import by.bsuir.mobileplatforms2.service.impl.ProductServiceImpl;
import by.bsuir.mobileplatforms2.utils.PriceUtils;

public class ProductListFragment extends Fragment {
    private Map<Integer, Integer> detailsButtonIdToIndex;
    private Product[] products;

    public ProductListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Прочитать аргуметы
        Bundle arguments = getArguments();
        products = (Product[]) arguments.getSerializable("products");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayProductListItems();
    }

    private void displayProductListItems() {
        detailsButtonIdToIndex = new HashMap<>();
        // Отобразить элементы
        LinearLayout linearLayout = getView().findViewById(R.id.productList_linearLayout);
        for (int i = 0; i < products.length; i++) {
            // Создание элемента
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.item_product_list, null);
            Button detailsButton = tableRow.findViewById(R.id.productList_item_button_details);
            detailsButton.setText(products[i].getTitle());
            detailsButton.setOnClickListener(this::detailsButtonOnClickListener);
            TextView priceTextView = tableRow.findViewById(R.id.productList_item_textView_price);
            priceTextView.setText(
                    PriceUtils.convert(products[i].getPrice()) + " " +
                            ApplicationState.ExchangeRate.getActiveCurrency()
            );
            // Установка id
            int id = View.generateViewId();
            detailsButton.setId(id);
            detailsButtonIdToIndex.put(id, i);
            // Отображение
            linearLayout.addView(tableRow);
        }
    }

    private void detailsButtonOnClickListener(View view) {
        // Подготовка аргументов
        Bundle args = new Bundle();
        int index = detailsButtonIdToIndex.get(view.getId());
        args.putSerializable("product", products[index]);
        // Переход
        NavHostFragment.findNavController(this).navigate(
                R.id.action_productListFragment_to_productDetailsFragment, args
        );
    }
}