package by.bsuir.mobileplatforms2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import by.bsuir.mobileplatforms2.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        // Прочитать аргументы

        String url = getIntent().getStringExtra("productURL");
        // Отобразить
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}