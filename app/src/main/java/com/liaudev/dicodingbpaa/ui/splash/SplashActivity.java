package com.liaudev.dicodingbpaa.ui.splash;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import com.liaudev.dicodingbpaa.App;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.databinding.ActivitySplashBinding;
import com.liaudev.dicodingbpaa.ui.BaseActivity;
import com.liaudev.dicodingbpaa.ui.login.LoginActivity;
import com.liaudev.dicodingbpaa.ui.main.MainActivity;


/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private boolean mShouldFinish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //change statusbar color
        updateStatusBarColor(getColor(R.color.transparent));
        //animate
        ObjectAnimator logo = ObjectAnimator.ofFloat(binding.imgLogo,View.TRANSLATION_Y,-150f,0).setDuration(2000);
        ObjectAnimator objTitle = ObjectAnimator.ofFloat(binding.tvTitle,View.ALPHA,1f).setDuration(500);
        ObjectAnimator objSubTitle = ObjectAnimator.ofFloat(binding.tvSubTitle,View.ALPHA,1f).setDuration(500);
        ObjectAnimator objVersion = ObjectAnimator.ofFloat(binding.tvVersion,View.TRANSLATION_Y,150f,0).setDuration(500);
        logo.setInterpolator(new BounceInterpolator());
        logo.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //to activity
                Intent intent;
                if(App.getInstance().isLogin()){
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View, String>(binding.imgLogo, "logo_image");
                        pairs[1] = new Pair<View, String>(binding.tvTitle, "logo_title");
                        pairs[2] = new Pair<View, String>(binding.tvSubTitle, "logo_subtitle");

                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                        startActivity(intent, activityOptions.toBundle());
                    }else{
                        startActivity(intent);
                    }
                }

                mShouldFinish = true;

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        logo.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(objTitle,objSubTitle,objVersion);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mShouldFinish)
            finish();
    }
}