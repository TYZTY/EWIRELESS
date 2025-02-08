package com.openpositioning.PositionMe.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.openpositioning.PositionMe.R;

public class ReplayFragment extends Fragment {

    private GoogleMap mMap;
    private Button playPauseButton, restartButton;
    private ProgressBar progressBar;
    private boolean isPlaying = false;

    public ReplayFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_replay, container, false);

        // Initialize UI elements
        playPauseButton = view.findViewById(R.id.play_pause_button);
        restartButton = view.findViewById(R.id.restart_button);
        progressBar = view.findViewById(R.id.progress_bar);

        // Set button click listeners
        playPauseButton.setOnClickListener(v -> togglePlayback());
        restartButton.setOnClickListener(v -> restartPlayback());

        // Set up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(onMapReadyCallback);
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取传递的文件路径
        if (getArguments() != null) {
            String trajectoryFilePath = getArguments().getString("trajectoryFilePath");
            // 解析文件并准备轨迹数据
            loadTrajectoryFile(trajectoryFilePath);
        }
    }

    private void loadTrajectoryFile(String filePath) {
        // 示例：加载轨迹文件
        if (filePath != null) {
            // TODO: 实现文件解析逻辑
            Log.d("ReplayFragment", "加载轨迹文件：" + filePath);
        }
    }



    private final OnMapReadyCallback onMapReadyCallback = googleMap -> {
        mMap = googleMap;

        // Add a test marker and polyline
        LatLng startPoint = new LatLng(37.422, -122.084); // Example coordinates
        LatLng endPoint = new LatLng(37.423, -122.085);

        mMap.addMarker(new MarkerOptions().position(startPoint).title("Start Point"));
        mMap.addPolyline(new PolylineOptions().add(startPoint, endPoint).color(getResources().getColor(R.color.primaryBlue)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15));
    };

    private void togglePlayback() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            playPauseButton.setText(R.string.pause);
            // Start replay logic here
        } else {
            playPauseButton.setText(R.string.play);
            // Pause replay logic here
        }
    }

    private void restartPlayback() {
        // Restart replay logic here
        playPauseButton.setText(R.string.play);
        isPlaying = false;
        progressBar.setProgress(0);
    }
}
