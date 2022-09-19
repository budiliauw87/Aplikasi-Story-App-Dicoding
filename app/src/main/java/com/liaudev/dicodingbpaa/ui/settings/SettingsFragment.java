package com.liaudev.dicodingbpaa.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.liaudev.dicodingbpaa.App;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.ui.login.LoginActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        //Select Theme
        ((ListPreference) findPreference("theme_mode")).setOnPreferenceChangeListener((preference, newValue) -> {
            String themeOption = (String) newValue;
            if(!App.getInstance().getThemeMode().equals(themeOption)){
                App.getInstance().saveThemeMode(themeOption);
                App.getInstance().applyTheme();
            }
            return true;
        });

        ((ListPreference) findPreference("language_app")).setOnPreferenceChangeListener((preference, newValue) -> {
            String languageOption = (String) newValue;
            if(!App.getInstance().getLangApp().equals(languageOption)){
                App.getInstance().saveLanguage(languageOption);
                getActivity().recreate();
            }
            return true;
        });
        ((Preference) findPreference("logout")).setOnPreferenceClickListener((preference) -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.logout_descreption))
                    .setPositiveButton(android.R.string.yes, (dialog, which)-> {
                        App.getInstance().resetMyPref();
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                        getActivity().finish();
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        });


    }

}