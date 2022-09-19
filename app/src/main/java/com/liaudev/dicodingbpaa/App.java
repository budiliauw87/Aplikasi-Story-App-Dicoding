package com.liaudev.dicodingbpaa;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class App extends Application {
    private static App mInstance;
    private SharedPreferences sharedPref;

    private String userId;
    private String token;
    private String userName;
    private String emailUser;
    private boolean isLogin;

    private String langApp;
    private String themeMode;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //encrypt sharepref
        initApp();
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    private void initApp() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPref = EncryptedSharedPreferences.create(
                    "dicodingbpaa",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            this.readMyPref();
            this.applyTheme();
            this.applyLang();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyLang() {
        final String currentLang = getLangApp();
    }

    // read pref
    public void readMyPref() {
        this.setLangApp(sharedPref.getString("language_app", "en"));
        this.setThemeMode(sharedPref.getString("theme_mode", "default"));
        this.setUserId(sharedPref.getString("userid", ""));
        this.setUserName(sharedPref.getString("username", ""));
        this.setEmailUser(sharedPref.getString("email", ""));
        this.setToken(sharedPref.getString("token", ""));
        this.setIsLogin(sharedPref.getBoolean("login", false));

    }

    //save pref
    public void saveMyPref(String userId, String username, String token, String email, boolean isLogin) {
        this.setUserId(userId);
        this.setToken(token);
        this.setUserName(username);
        this.setEmailUser(email);
        this.setIsLogin(isLogin);
        sharedPref.edit().putString("email", email).apply();
        sharedPref.edit().putString("userid", userId).apply();
        sharedPref.edit().putString("username", username).apply();
        sharedPref.edit().putString("token", token).apply();
        sharedPref.edit().putBoolean("login", isLogin).apply();
    }

    //set default val sharepref
    public void resetMyPref() {
        this.setUserId("");
        this.setToken("");
        this.setIsLogin(false);
        this.setUserName("");
        this.setEmailUser("");
        sharedPref.edit().putString("email", "").apply();
        sharedPref.edit().putString("userid", "").apply();
        sharedPref.edit().putString("username", "").apply();
        sharedPref.edit().putString("token", "").apply();
        sharedPref.edit().putBoolean("login", false).apply();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLangApp() {
        return langApp;
    }

    public void setLangApp(String langApp) {
        this.langApp = langApp;
    }

    public String getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(String themeMode) {
        this.themeMode = themeMode;
    }

    public void saveThemeMode(String mode) {
        this.setThemeMode(mode);
        sharedPref.edit().putString("theme_mode", mode).apply();
    }

    public void saveLanguage(String language) {
        this.setLangApp(language);
        sharedPref.edit().putString("language_app", language).apply();
    }

    public void applyTheme() {
        String themeMode = this.getThemeMode();

        switch (themeMode) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
