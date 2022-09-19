package com.liaudev.dicodingbpaa.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.liaudev.dicodingbpaa.App;

import java.util.Locale;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class BaseActivity extends AppCompatActivity {

    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window = getWindow();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(useCustomConfig(newBase));
    }
    public void updateStatusBarColor(int color) {
        try {
            if(window == null){
                window = getWindow();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else{
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Context useCustomConfig(Context context) {
        final Locale locale = new Locale(App.getInstance().getLangApp());
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= 17) {
            Configuration config = new Configuration();
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            Resources res = context.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
            return context;
        }
    }
}
