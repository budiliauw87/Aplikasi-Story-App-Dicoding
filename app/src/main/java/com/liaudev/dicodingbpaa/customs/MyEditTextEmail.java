package com.liaudev.dicodingbpaa.customs;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.liaudev.dicodingbpaa.R;

/**
 * Created by Budiliauw87 on 2022-05-17.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class MyEditTextEmail extends AppCompatEditText {
    private boolean isHasError;

    public MyEditTextEmail(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public MyEditTextEmail(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyEditTextEmail(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public boolean isHasError(){
        return this.isHasError;
    }

    private void init(final Context context, AttributeSet attrs){
        isHasError = true;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    setError(context.getString(R.string.please_insert_field));
                    isHasError = true;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(charSequence.toString()).matches()){
                    setError(context.getString(R.string.invalid_email));
                    isHasError = true;
                }else{
                    setError(null);
                    isHasError = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
