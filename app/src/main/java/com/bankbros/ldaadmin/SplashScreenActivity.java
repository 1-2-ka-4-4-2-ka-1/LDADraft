package com.bankbros.ldaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {


    AnimatedVectorDrawable avd1;
    AnimatedVectorDrawableCompat avd2;

    public ImageView splash_logo;
    public static Activity splash_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        splash_activity = this;


        splash_logo = findViewById(R.id.splash_logo);
        animate(splash_logo);



    }

    public void animate(final ImageView view) {


        ImageView v = view;
        Drawable d = v.getDrawable();


        if (d instanceof AnimatedVectorDrawableCompat) {

            avd2 = (AnimatedVectorDrawableCompat) d;
            avd2.start();
            avd2.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    avd2.stop();
                    goTonext();

                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (d instanceof AnimatedVectorDrawable) {
                avd1 = (AnimatedVectorDrawable) d;
                avd1.start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avd1.registerAnimationCallback(new Animatable2.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            avd1.stop();
                            goTonext();

                        }
                    });
                }
            } else {
                goTonext();
            }
        }
    }

    public void goTonext(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent = new Intent(SplashScreenActivity.this,DashBoardActivity.class);
                    startActivity(intent);
                    Log.i("Login", "run: "+"success");
                }else {
                    Intent intent = new Intent(SplashScreenActivity.this,LoginActvity.class);
                    startActivity(intent);
                }
            }
        },1500);

    }
}
