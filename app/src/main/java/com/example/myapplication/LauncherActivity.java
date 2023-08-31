package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.view.CardInputWidget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Button launchCheckoutJava = findViewById(R.id.launch_checkout_java);

        launchCheckoutJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(CheckoutActivity.class);
            }
        });

    }

    private void launchActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }


}