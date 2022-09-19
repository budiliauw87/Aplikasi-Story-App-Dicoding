package com.liaudev.dicodingbpaa.customs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.liaudev.dicodingbpaa.R;

/**
 * Created by Budiliauw87 on 2022-05-28.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context) {
        super(context);
        setCancelable(false);
        View view = LayoutInflater.from(context).inflate(
                R.layout.progress_dialog_view, null);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView cp_title = view.findViewById(R.id.cp_title);
        cp_title.setText(context.getString(R.string.loading_msg));
        setContentView(view);
    }
}
