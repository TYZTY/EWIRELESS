package com.openpositioning.PositionMe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MASTER_API_KEY = BuildConfig.OPENPOSITIONING_MASTER_KEY;
    private static final String USER_API_KEY = BuildConfig.OPENPOSITIONING_API_KEY;
    private GoogleMap mMap;
    private List<LatLng> pathPoints;

    private void uploadTrackingData() {
        if (pathPoints.isEmpty()) {
            Toast.makeText(this, "No tracking data to upload", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("api_key", USER_API_KEY);
            jsonBody.put("master_key", MASTER_API_KEY);
            JSONArray coordinates = new JSONArray();
            for (LatLng point : pathPoints) {
                JSONObject coordinate = new JSONObject();
                coordinate.put("latitude", point.latitude);
                coordinate.put("longitude", point.longitude);
                coordinates.put(coordinate);
            }
            jsonBody.put("coordinates", coordinates);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String url = "https://api.openpositioning.org/tracks";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Upload failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Master-Key", MASTER_API_KEY);
                headers.put("X-API-Key", USER_API_KEY);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(v -> uploadTrackingData());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}