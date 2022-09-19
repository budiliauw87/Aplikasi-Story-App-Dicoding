package com.liaudev.dicodingbpaa.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Budiliauw87 on 2022-05-23.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static byte[] getFileData(File file) {
        if (file != null) {
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            byte[] tmpBuff = new byte[size];

            try {
                FileInputStream fis = new FileInputStream(file);
                int read = fis.read(bytes, 0, size);
                if (read < size) {
                    int remain = size - read;
                    while (remain > 0) {
                        read = fis.read(tmpBuff, 0, remain);
                        System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                        remain -= read;
                    }
                }
                fis.close();
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return Color.rgb(r, g, b);
    }
}
