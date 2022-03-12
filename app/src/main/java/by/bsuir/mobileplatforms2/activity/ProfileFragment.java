package by.bsuir.mobileplatforms2.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import by.bsuir.mobileplatforms2.ApplicationState;
import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.service.ImageLoaderService;
import by.bsuir.mobileplatforms2.service.impl.CachingImageLoaderService;

public class ProfileFragment extends Fragment {
    private ImageLoaderService cachedImageService;

    private Switch darkModeSwitch;
    private Spinner currencySpinner;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Инициализация
        cachedImageService = new CachingImageLoaderService(getContext());
        darkModeSwitch = view.findViewById(R.id.profile_switch_darkMode);
        currencySpinner = view.findViewById(R.id.profile_spinner_currency);
        // Начать фоновую загрузку ресурсов
        Future<Bitmap> profileImage = cachedImageService.loadImage(ApplicationState.UserProfile.getPicture().toString());
        // Настройка UI
        setupUi(view);
        // Установить слушатели
        darkModeSwitch.setOnCheckedChangeListener(this::darkModeSwitchOnCheckedChangeListener);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                String currency = (String) adapterView.getItemAtPosition(position);
                ApplicationState.ExchangeRate.setActiveCurrency(currency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // ignore
            }
        });
        // После подгрузки ресурсов
        setProfileImage(view, profileImage);
    }

    private void setupUi(View view) {
        TextView firstNameTextView = view.findViewById(R.id.profile_textView_firstName);
        firstNameTextView.setText(firstNameTextView.getText() + ApplicationState.UserProfile.getFirstName());
        TextView lastNameTextView = view.findViewById(R.id.profile_textView_lastName);
        lastNameTextView.setText(lastNameTextView.getText() + ApplicationState.UserProfile.getLastName());
        TextView emailTextView = view.findViewById(R.id.profile_textView_emailName);
        emailTextView.setText(emailTextView.getText() + ApplicationState.UserProfile.getUsername());
        darkModeSwitch.setChecked(isDarkModeOn());
        // Добавить валюты
        Object[] items = ApplicationState.ExchangeRate.getExchangeRates().keySet().toArray();
        currencySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items));
        // Выделить активную валюту
        String activeCurrency = ApplicationState.ExchangeRate.getActiveCurrency();
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(activeCurrency)) {
                currencySpinner.setSelection(i);
                break;
            }
        }
    }

    private boolean isDarkModeOn() {
        return (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES;
    }

    private void darkModeSwitchOnCheckedChangeListener(CompoundButton compoundButton, boolean b) {
        if (b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setProfileImage(View view, Future<Bitmap> profileImage) {
        ImageView imageView = view.findViewById(R.id.profile_imageView);
        try {
            Bitmap bitmap = profileImage.get();
            // Проверка наличия изображения
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getContext(), "Failed to load profile image", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}