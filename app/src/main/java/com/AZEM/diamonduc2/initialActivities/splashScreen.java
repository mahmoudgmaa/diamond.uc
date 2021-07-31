package com.AZEM.diamonduc2.initialActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.AZEM.diamonduc2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class splashScreen extends AppCompatActivity {
    private static int time_out = 2000;
    private ImageView img;
    private TextView name,powered;
    private Animation top, bottom;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private static boolean opened = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (opened) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

//        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//        boolean firstStart = preferences.getBoolean("firstStart", true);
//        if (firstStart) {
//            auth.signOut();
//            SharedPreferences preferences2 = getSharedPreferences("prefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences2.edit();
//            editor.putBoolean("firstStart", false);
//            editor.apply();
//        }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        StartAppSDK.init(this, "208479601", true);
        StartAppAd.disableSplash();
        img = findViewById(R.id.appImage);
        name = findViewById(R.id.nameOfApp);
        powered = findViewById(R.id.powered);
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this, firstScreen.class);
                startActivity(intent);
                opened = true;
                finish();
            }
        }, time_out);

    }
}