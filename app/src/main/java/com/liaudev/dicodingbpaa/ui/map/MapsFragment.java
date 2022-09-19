package com.liaudev.dicodingbpaa.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.customs.CustomProgressDialog;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.databinding.FragmentMapsBinding;
import com.liaudev.dicodingbpaa.viewmodel.ViewModelFactory;

import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapViewModel mapViewModel;
    private CustomProgressDialog customProgressDialog;

    //request permission using activitylaunher
    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        mMap.setMyLocationEnabled(true);
                        getLocationStory();
                    } else {
                        Snackbar.make(getView(), getString(R.string.permission_location_title), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(android.R.string.ok), (v) -> {
                                    mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                                }).show();
                    }
                }
            });
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMapsBinding binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
            mapViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) factory).get(MapViewModel.class);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        changeMapStyle();
        enableStateMyLocationBtn();
    }

    private void getLocationStory() {
        mapViewModel.getStoryLocation(3).observe(getActivity(),response->{
            switch (response.status){
                case ERROR:
                    customProgressDialog.hide();
                    Snackbar.make(getView(), response.message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(android.R.string.ok), (v) -> {
                                getLocationStory();
                            }).show();
                    break;
                case LOADING:
                    if (customProgressDialog == null) {
                        customProgressDialog = new CustomProgressDialog(getActivity());
                        customProgressDialog.setCancelable(false);
                        customProgressDialog.setCanceledOnTouchOutside(false);
                    }
                    customProgressDialog.show();
                    break;
                case SUCCESS:
                    try {
                        if(response.data!=null){
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            List<StoryEntity> entityList = response.data.getListStory();

                            for(StoryEntity entity:entityList){
                                //change length lat lon
                                final double finalLat = Double.parseDouble(String.format("%.6f", entity.getLat()));
                                final double finalLon = Double.parseDouble(String.format("%.6f", entity.getLon()));
                                final LatLng latLng = new LatLng(finalLat,finalLon);
                                mMap.addMarker(new MarkerOptions().position(latLng).title(entity.getName()));
                                builder.include(latLng);
                            }
                            LatLngBounds tmpBounds = builder.build();
                            //tmpBounds.getCenter();
                            mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngBounds(
                                            tmpBounds,
                                            getResources().getDisplayMetrics().widthPixels,
                                            getResources().getDisplayMetrics().heightPixels,
                                            300)
                            );
                        }
                        customProgressDialog.hide();
                    }catch (Exception e){
                        e.printStackTrace();
                        customProgressDialog.hide();
                    }

                    break;
            }
        });

    }

    private void changeMapStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.map_style));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableStateMyLocationBtn() {
        if (hasPermissionLocation()) {
            mMap.setMyLocationEnabled(true);
            getLocationStory();
        }else{
            requestPermissions();
        }
    }

    private boolean hasPermissionLocation(){
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }



    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(getView(), getString(R.string.permission_location_title), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(android.R.string.ok), (v) -> {
                        mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }).show();
        } else {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}