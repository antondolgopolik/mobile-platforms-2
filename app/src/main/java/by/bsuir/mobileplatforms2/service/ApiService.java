package by.bsuir.mobileplatforms2.service;

import com.android.volley.Response;

public interface ApiService {

    void sendGetRequest(String url,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener);
}
