package com.zxdmjr.placefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.zxdmjr.placefinder.custom_activity.CustomActivity;

public class SplashScreen extends CustomActivity {

    TextView textView;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        textView = (TextView) findViewById(R.id.yonderFinder);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        textView.startAnimation(animation);

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent goMain = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(goMain);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}