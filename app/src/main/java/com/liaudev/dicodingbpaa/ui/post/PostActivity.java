package com.liaudev.dicodingbpaa.ui.post;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.snackbar.Snackbar;
import com.liaudev.dicodingbpaa.App;
import com.liaudev.dicodingbpaa.Constants;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.customs.CustomProgressDialog;
import com.liaudev.dicodingbpaa.databinding.ActivityPostBinding;
import com.liaudev.dicodingbpaa.ui.BaseActivity;
import com.liaudev.dicodingbpaa.viewmodel.ViewModelFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends BaseActivity {
    ActivityPostBinding binding;
    private PostViewModel postViewModel;
    private boolean isEdited = false;
    private boolean isPostByGuest = false;
    private String currentPhotoPath;
    private int METHOD_PICKER_IMAGE = 0; // 0 CAMERA, 1 GALLERY for permissionresult
    private CustomProgressDialog customProgressDialog;

    private double lat = 0.0, lon = 0.0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isRunningUpdateLocation = false, isRequestUpdateLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        postViewModel = new ViewModelProvider(PostActivity.this, (ViewModelProvider.Factory) factory).get(PostViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final String name = App.getInstance().getUserName();
        final String firstCharName = Character.toString(name.charAt(0)).toUpperCase(Locale.ROOT);
        binding.tvUsername.setText(name);
        binding.tvRoundName.setText(firstCharName);
        binding.tvPostBy.setText(String.format(getString(R.string.post_by_format), name));

        // take foto from camera capture
        binding.tvAddPhotoCamera.setOnClickListener((v) -> {
            if (!checkPermissions()) {
                METHOD_PICKER_IMAGE = 0;
                requestPermissionsPosting();
            } else {
                openCamera();
            }
        });

        // take foto from gallery
        binding.tvAddPhotoGallery.setOnClickListener((v) -> {
            if (!checkPermissions()) {
                METHOD_PICKER_IMAGE = 1;
                requestPermissionsPosting();
            } else {
                openGalley();
            }
        });
        binding.checkboxGuest.setOnCheckedChangeListener((compoundButton, bool)-> isPostByGuest = bool );
        binding.btnPosting.setOnClickListener((v) -> {
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        });

        binding.btnRemovePhoto.setOnClickListener((v) -> {
            binding.parentListPostMenu.setVisibility(VISIBLE);
            binding.parentPhoto.setVisibility(GONE);
            currentPhotoPath = "";
        });

        binding.btnPosting.setOnClickListener((v) -> {
            postingStory();
        });
        binding.tvAddLocation.setOnClickListener((v) -> {
            if(isRunningUpdateLocation && isRequestUpdateLocation){
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                isRunningUpdateLocation = false;
                isRequestUpdateLocation = false;
                binding.tvAddLocation.setText(getString(R.string.add_location));
            }else{
                getMyLocation();
            }

        });

        binding.edtDescreption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isEdited) isEdited = true;
                if (charSequence.toString().isEmpty()) {
                    binding.edtDescreption.setError(getString(R.string.please_insert_field));
                } else if (charSequence.toString().length() <= 6) {
                    binding.edtDescreption.setError(getString(R.string.minim_six_char));
                } else {
                    binding.edtDescreption.setError(null);
                    binding.btnPosting.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initLocation();

    }
    private void initLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(100);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        builder.setAlwaysShow(true);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if(location!=null){
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        binding.tvInfoLocation.setText(lat+", "+lon);
                    }
                }
            }
        };
    }

    //get location
    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        if (!hasPermissionLocation()) {
            requestPermissions();
        } else {
            binding.tvAddLocation.setText(getString(R.string.stop_request));
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
            isRequestUpdateLocation = true;
            isRunningUpdateLocation = true;
        }
    }

    private boolean hasPermissionLocation() {
        return ActivityCompat.checkSelfPermission(PostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(PostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(PostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(binding.coordinator, getString(R.string.permission_location_title), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(android.R.string.ok), (v) -> {
                        mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }).show();
        } else {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    //request permission using activitylaunher
    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        getMyLocation();
                    } else {
                        Snackbar.make(binding.coordinator, getString(R.string.permission_location_title), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(android.R.string.ok), (v) -> {
                                    mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                                }).show();
                    }
                }
            });

    private void postingStory() {
        final String des = binding.edtDescreption.getText().toString();
        //validate picture must be insert
        if (des.isEmpty()) {
            Toast.makeText(PostActivity.this, getString(R.string.please_insert_field), Toast.LENGTH_SHORT).show();
            return;
        }
        //validate picture must be insert
        if (currentPhotoPath.isEmpty() || currentPhotoPath == null) {
            Toast.makeText(PostActivity.this, getString(R.string.please_insert_photo), Toast.LENGTH_SHORT).show();
            return;
        }

        postViewModel.postStory(des, new File(currentPhotoPath),lat,lon, isPostByGuest).observe(this, baseResponseResource -> {
            switch (baseResponseResource.status) {
                case ERROR:
                    customProgressDialog.hide();
                    String error = baseResponseResource.message;
                    if (error == null || error.isEmpty()) {
                        error = getString(R.string.something_error);
                    }
                    Toast.makeText(PostActivity.this, error, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    customProgressDialog.hide();
                    Toast.makeText(PostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent result = new Intent();
                    setResult(Activity.RESULT_OK, result);
                    finish();
                    break;
                case LOADING:
                    if (customProgressDialog == null) {
                        customProgressDialog = new CustomProgressDialog(PostActivity.this);
                        customProgressDialog.setCancelable(false);
                        customProgressDialog.setCanceledOnTouchOutside(false);
                    }
                    customProgressDialog.show();
                    break;
            }
        });

    }

    private void openGalley() {
        Intent takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        takePictureIntent.setType("image/*");
        activityResultLauncherGallery.launch(Intent.createChooser(takePictureIntent, "Choose picture"));
    }

    // Activityresult for gallery
    private final ActivityResultLauncher<Intent> activityResultLauncherGallery
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Uri uriGallery = result.getData().getData();
            if (uriGallery != null) {
                try {
                    File photoFile = createImageFile();
                    InputStream inputStream = getContentResolver().openInputStream(uriGallery);
                    OutputStream outputStream = new FileOutputStream(photoFile);
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    // close file output
                    outputStream.close();
                    inputStream.close();
                    binding.parentListPostMenu.setVisibility(GONE);
                    binding.parentPhoto.setVisibility(VISIBLE);

                    Uri currentUriPhoto = Uri.parse(currentPhotoPath);
                    binding.imgPhoto.setImageURI(currentUriPhoto);
                    if (!isEdited) isEdited = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }));

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.liaudev.dicodingbpaa",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activityResultLauncherCamera.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(this, "Failed open camera", Toast.LENGTH_LONG).show();
        }

    }

    // Activityresult for camera
    private final ActivityResultLauncher<Intent> activityResultLauncherCamera
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            if (bitmap != null) {
                binding.parentListPostMenu.setVisibility(GONE);
                binding.parentPhoto.setVisibility(VISIBLE);
                binding.imgPhoto.setImageBitmap(bitmap);
                if (!isEdited) isEdited = true;
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.something_error), Toast.LENGTH_SHORT).show();
            }
        }
    }));

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    // show confrim dialog when has edited
    private void checkIsEdited() {
        if (isEdited) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.cancel))
                    .setMessage(getString(R.string.cancel_posting))
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        finish();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            finish();
        }
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }

    }

    private void requestPermissionsPosting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    Constants.REQUEST_PERMISSIONS_POSTING_CODE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_PERMISSIONS_POSTING_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSIONS_POSTING_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (METHOD_PICKER_IMAGE == 0) {
                        openCamera();
                    } else {
                        openGalley();
                    }
                } else {
                    Snackbar.make(binding.coordinator, getString(R.string.permission_required), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.try_again), (v) -> {
                                requestPermissionsPosting();
                            }).show();
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (METHOD_PICKER_IMAGE == 0) {
                        openCamera();
                    } else {
                        openGalley();
                    }
                } else {
                    Snackbar.make(binding.coordinator, getString(R.string.permission_required), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.try_again), (v) -> {
                                requestPermissionsPosting();
                            }).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            checkIsEdited();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        checkIsEdited();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if(isRequestUpdateLocation){
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
            isRunningUpdateLocation = true;
        }
    }

    @Override
    public void onPause(){
        if(isRunningUpdateLocation){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        super.onPause();
    }
}