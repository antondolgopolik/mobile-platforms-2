package by.bsuir.mobileplatforms2.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.Map;

import by.bsuir.mobileplatforms2.ApplicationState;
import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.service.ExchangeRateService;
import by.bsuir.mobileplatforms2.service.UserService;
import by.bsuir.mobileplatforms2.service.impl.ExchangeRateServiceImpl;
import by.bsuir.mobileplatforms2.service.impl.UserServiceImpl;

public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST_CODE = 1;
    private static boolean firstCreate = true;

    private SignInClient signInClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Установка содержимого
        setContentView(R.layout.activity_main);
        // Настройки при первом создании
        if (firstCreate) {
            firstCreate = false;
            onFirstCreate();
        }
        // Настройка навигации
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        BottomNavigationView navigationView = findViewById(R.id.main_bottomNavView);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void onFirstCreate() {
        // Потребовать логин
        login();
        // Запрос на получение курсов
        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl(this);
        exchangeRateService.requestAllExchangeRates(
                ApplicationState.ExchangeRate.getActiveCurrency(),
                this::allExchangeRatesListener, this::allExchangeRatesErrorListener
        );
    }

    private void login() {
        signInClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.google_server_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();
        signInClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), LOGIN_REQUEST_CODE,
                                null, 0, 0, 0
                        );
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        finishAndRemoveTask();
                    }
                })
                .addOnFailureListener(this, e -> {
                    e.printStackTrace();
                    finishAndRemoveTask();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            try {
                SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
                String username = credential.getId();
                // Создать пользователя
                UserService userService = new UserServiceImpl(this);
                userService.createUser(username);
                // Сохранить информацию о профиле
                ApplicationState.UserProfile.setUsername(username);
                ApplicationState.UserProfile.setFirstName(credential.getGivenName());
                ApplicationState.UserProfile.setLastName(credential.getFamilyName());
                ApplicationState.UserProfile.setPicture(credential.getProfilePictureUri());
            } catch (ApiException e) {
                e.printStackTrace();
                finishAndRemoveTask();
            }
        }
    }

    private void allExchangeRatesListener(Map<String, Double> exchangeRates) {
        exchangeRates.put(ApplicationState.ExchangeRate.getActiveCurrency(), 1D);
        ApplicationState.ExchangeRate.setExchangeRates(exchangeRates);
    }

    private void allExchangeRatesErrorListener(VolleyError error) {
        ApplicationState.ExchangeRate.setExchangeRates(
                Collections.singletonMap(ApplicationState.ExchangeRate.getActiveCurrency(), 1D)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_favorite) {
            startActivity(new Intent(this, FavoriteActivity.class));
            return true;
        }
        if (itemId == R.id.action_about_us) {
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}