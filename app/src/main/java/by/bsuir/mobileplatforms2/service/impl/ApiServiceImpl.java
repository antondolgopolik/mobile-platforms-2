package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;
import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import by.bsuir.mobileplatforms2.service.ApiService;

public class ApiServiceImpl implements ApiService {
    private final Context context;

    public ApiServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void sendGetRequest(String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(request);
    }
}
