package com.liaudev.dicodingbpaa.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liaudev.dicodingbpaa.Constants;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.databinding.ActivityDetailBinding;
import com.liaudev.dicodingbpaa.ui.BaseActivity;
import com.liaudev.dicodingbpaa.viewmodel.ViewModelFactory;

import java.util.Locale;

/**
 * Created by Budiliauw87 on 2022-06-08.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private DetailViewModel detailViewModel;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        detailViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) factory).get(DetailViewModel.class);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        StoryEntity storyEntity = getIntent().getParcelableExtra(Constants.EXTRA_STORY);
        final String name = storyEntity.getName();
        final String firstCharName = Character.toString(name.charAt(0)).toUpperCase(Locale.ROOT);
        binding.tvUsername.setText(name);
        binding.tvRoundName.setText(firstCharName);

        binding.tvCreatedAt.setText(storyEntity.getCreatedAt());
        binding.tvDescreption.setText(storyEntity.getDescription());

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_launcher_foreground);
        options.centerCrop();
        Glide.with(DetailActivity.this)
                .load(storyEntity.getPhotoUrl()) // Load the image
                .thumbnail(Glide.with(this)
                        .asDrawable().sizeMultiplier(0.25f))
                .apply(options)
                .into(binding.imgPhoto);

        binding.btnShare.setOnClickListener((v) -> {
            final String appName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        });

        binding.btnFavorite.setOnClickListener((v) -> {
            if (isFavorite) {
                detailViewModel.deleteFavorite(storyEntity.getId());
            } else {
                detailViewModel.setFavorite(new FavoriteEntity(storyEntity.getId()));
            }
        });

        detailViewModel.getFavorite(storyEntity.getId()).observe(this, entity -> {
            if (entity == null) {
                binding.btnFavorite.setIconTint(ContextCompat.getColorStateList(this, R.color.white));
                isFavorite = false;
            } else {
                binding.btnFavorite.setIconTint(ContextCompat.getColorStateList(this, android.R.color.holo_red_dark));
                isFavorite = true;
            }
        });
        Log.e("Detail","lat "+storyEntity.getLat()+"\nLon "+storyEntity.getLon());
        binding.imgLocation.setOnClickListener((v) -> {
            if (storyEntity.getLat() != null || storyEntity.getLon() != null ) {
                try {
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("www.google.com")
                            .appendPath("maps")
                            .appendPath("dir")
                            .appendPath("")
                            .appendQueryParameter("api", "1")
                            .appendQueryParameter("destination", 80.00023 + "," + 13.0783);
                    String url = builder.build().toString();
                    Intent intentGoogleMap = new Intent(Intent.ACTION_VIEW);
                    intentGoogleMap.setData(Uri.parse(url));
                    startActivity(intentGoogleMap);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.no_cordinate_location), Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
