package com.example.virtualassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //Splash screen duration variable (1000 equals 1 second)
    public static int SPLASH_SCREEN = 3000;
    //Variable declaration for animations
    Animation topAnimation, bottomAnimation;
    //Variable declaration for layout objects
    ImageView image;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign the animations created in the 'anim' resource folder to variables
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Link image/appName variables to layout objects
        image = findViewById(R.id.splash_screen_logo);
        message = findViewById(R.id.welcomeMessage);

        //Assign animations to the image/appName layout objects
        image.setAnimation(topAnimation);
        message.setAnimation(bottomAnimation);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
}
