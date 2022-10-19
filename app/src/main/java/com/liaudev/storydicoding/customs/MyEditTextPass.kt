package com.liaudev.storydicoding.customs

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.liaudev.storydicoding.R


/**
 * Created by Budiliauw87 on 2022-10-20.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
class MyEditTextPass : AppCompatEditText {
    var isHasError: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    isHasError = true
                    error = context.getString(R.string.please_insert_field)
                }else if(s.length < 6){
                    error = context.getString(R.string.enter_six_char)
                    isHasError = true
                }else{
                    error = null
                    isHasError = false
                }

            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}