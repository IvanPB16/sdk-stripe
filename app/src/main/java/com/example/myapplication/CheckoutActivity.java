package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmSetupIntentParams;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.CvcEditText;
import com.stripe.android.view.PostalCodeEditText;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Map;


public class CheckoutActivity extends AppCompatActivity {
    private String setupIntentClientSecret;
    private Stripe stripe;
    private PaymentLauncher paymentLauncher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final PaymentConfiguration paymentConfiguration = PaymentConfiguration.getInstance(getApplicationContext());
        paymentLauncher = PaymentLauncher.Companion.create(
                this,
                "pk_test_51NAb73HVWg5sdyHq0fzExq14Uxk5K11l7AdXi5Zrhhops4V5EqElJQw1TEDdLU6CC5j3Rjy7dTsH1aV7jMb9S5VF00rrjcpmBj",
                paymentConfiguration.getStripeAccountId(),
                this::onPaymentResult
        );
        loadPage();
    }

    private void loadPage() {
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            // Collect card details
            //CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            //Log.d("Prueba card cardInputWidget", "cardInputWidget " + cardInputWidget);
            //PaymentMethodCreateParams.Card card = cardInputWidget.getPaymentMethodCard();
            //Log.d("Prueba card", "card " + card);


            CardNumberEditText cardInput = findViewById(R.id.cardNumberEditText);
            Log.d("Prueba cardInput", "cardInput " + cardInput.getCardBrand());
            Log.d("Prueba cardInput", "cardInput getText " + cardInput.getText());
            ExpiryDateEditText expiryDateInput = findViewById(R.id.expiryDateEditText);
            String[] parts = expiryDateInput.getText().toString().split("/");
            Log.d("Prueba cardInput", "cardInput getText " + expiryDateInput.getText().toString());
            CvcEditText cvcInput = findViewById(R.id.cvcEditText);
            Log.d("Prueba cardInput", "cardInput getText " + cvcInput.getText().toString());
            PostalCodeEditText zipcodeInput = findViewById(R.id.zipCode);
            Log.d("Prueba cardInput", "cardInput getText " + zipcodeInput.getText().toString());
            /*
            EditText experireInput = findViewById(R.id.expiryDateEditText);
            Log.d("Prueba experireInput", "experireInput " + experireInput.toString());
            EditText cvcInput = findViewById(R.id.cvcEditText);
            Log.d("Prueba cvcInput", "cvcInput " + cvcInput.toString());
            */
            CardInputWidget cardInputWidget = new CardInputWidget(this);
            cardInputWidget.setCardNumber(cardInput.getText().toString());
            cardInputWidget.setExpiryDate( Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            cardInputWidget.setCvcCode(cvcInput.getText().toString());

            PaymentMethodCreateParams.Card card = cardInputWidget.getPaymentMethodCard();
            Log.d("Prueba card", "card " + card);

            // Later, you will need to attach the PaymentMethod to the Customer it belongs to.
            // This example collects the customer's email to know which customer the PaymentMethod belongs to, but your app might use an account id, session cookie, etc.
            EditText nameInput = findViewById(R.id.nameInput);
            Log.d("Prueba card emailInput", "emailInput " + nameInput);


            PaymentMethod.BillingDetails billingDetails = (new PaymentMethod.BillingDetails.Builder()).setName(nameInput.getText().toString()).build();
            Log.d("Prueba card billingDetails", "billingDetails " + billingDetails);
            if (card != null) {
                Log.d("Prueba if card", "card " + card);
                // Create SetupIntent confirm parameters with the above
                PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams
                        .create(card, billingDetails);
                Log.d("Prueba card paymentMethodParams", "paymentMethodParams " + paymentMethodParams.toString());
                ConfirmSetupIntentParams confirmParams = ConfirmSetupIntentParams.create(paymentMethodParams, "clientSecret");
                Log.d("Prueba confirmParams", "confirmParams " + confirmParams);
                paymentLauncher.confirm(confirmParams);
            }
        });
    }

    private void onPaymentResult(PaymentResult paymentResult) {
        String title = "";
        String message = "";
        boolean restartDemo = false;
        if (paymentResult instanceof PaymentResult.Completed) {
            title = "Setup Completed";
            restartDemo = true;
        } else if (paymentResult instanceof PaymentResult.Canceled) {
            title = "Setup Canceled";
        } else if (paymentResult instanceof PaymentResult.Failed) {
            title = "Setup Failed";
            message = ((PaymentResult.Failed) paymentResult).getThrowable().getMessage();
        }
        displayAlert(title, message, restartDemo);
    }

    private void displayAlert(String title, String message, Boolean restartDemo) {
        runOnUiThread(()-> {
            //final CardInputWidget cardInputWidget= findViewById(R.id.cardNumberEditText);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message);
            if (restartDemo) {
                builder.setPositiveButton("Restart Demo", (v1, v2) -> {
                   // cardInputWidget.clear();
                    loadPage();
                });
            } else {
                builder.setPositiveButton("Ok", null);
            }
            builder.create().show();
        });
    }

}
