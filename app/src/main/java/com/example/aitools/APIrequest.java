package com.example.aitools;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIrequest {

    private final String URI = "https://api.openai.com/v1/completions";
    private final String APIKey = "Bearer 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";
    private RequestQueue requestQueue;
    private OnResponseListener responseListener;
    private String textToProcess;


    public interface OnResponseListener {
        void onResponse(String text);
    }

    // Constructor method, initializes the variable requestQueue
    public APIrequest(Context context, String text) {
        requestQueue = Volley.newRequestQueue(context);
        textToProcess = text;
    }

    // Create request and send to openai API
    private JsonObjectRequest createRequest() throws JSONException {

        // Sets the temperature and the maximum number of characters from the Options class.
        JSONObject body = new JSONObject("{'model': 'text-davinci-002', 'temperature': " + Options.temperature  +  ", 'max_tokens': " + Options.max_tokens + " }");
        body.put("prompt", textToProcess);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,this.URI, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray res = (JSONArray) response.get("choices");

                    // Executes the onResponse method of the OnResponseListener interface and passes it the result as parameter
                    responseListener.onResponse(res.getJSONObject(0).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", APIKey);
                headers.put("OpenAI-Organization", "org-i8SVlVAWl0AefS0ADV4csKgo");
                return headers;
            }
        };

        return request;
    }

    // Adds the request to the requestQueue and sends it, receives as a parameter an onResponse interface that will be executed when the response is received.
    public void getAiResponse(OnResponseListener onResponse) {
        this.responseListener = onResponse;
        try {
            requestQueue.add(createRequest());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
