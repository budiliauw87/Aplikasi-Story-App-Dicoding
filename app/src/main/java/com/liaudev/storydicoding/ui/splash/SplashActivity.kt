package com.liaudev.storydicoding.ui.splash

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.liaudev.storydicoding.databinding.ActivitySplashBinding
import com.liaudev.storydicoding.ui.main.MainActivity

/**
 * Created by Budiliauw87 on 2022-10-19.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
class SplashActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        startAnimation()
    }

    private fun startAnimation() {

        val objTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val objSubTitle = ObjectAnimator.ofFloat(binding.tvSubTitle, View.ALPHA, 1f).setDuration(500)
        val objVersion = ObjectAnimator.ofFloat(binding.tvVersion, View.TRANSLATION_Y, 150f,0f).setDuration(500)

        val logo = ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_Y, -150f,0f).setDuration(2000)
        logo.interpolator = BounceInterpolator()
        AnimatorSet().apply {
            playSequentially(logo,objTitle, objSubTitle, objVersion)
            addListener(onEnd ={

                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            })
        }.start()

    }
}