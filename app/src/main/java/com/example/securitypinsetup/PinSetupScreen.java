package com.example.securitypinsetup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PinSetupScreen extends AppCompatActivity {

    private TextView txtLabel;
    private EditText edtPIN;
    private EditText edtConfirmPIN;
    private Button btnSetUp;
    private ImageView imgLogo;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_setup_screen);

        edtPIN = findViewById(R.id.edt_pin);
        edtConfirmPIN = findViewById(R.id.edt_confirm_pin);
        btnSetUp = findViewById(R.id.btn_set_pin);
        txtLabel = findViewById(R.id.txt_label);
        imgLogo = findViewById(R.id.img_logo);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int pin = sharedPreferences.getInt("securityPin", 0);
        if (pin == 0) {

            btnSetUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleSetUpButton();
                }
            });

        } else {

            txtLabel.setText("Please enter 4 digit pin to login to the app");
            edtConfirmPIN.setVisibility(View.GONE);
            btnSetUp.setText("Continue");

            Executor executor = Executors.newSingleThreadExecutor();
            final BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        System.out.println("click on negitive button");
                    }
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    callMainActivity();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    System.out.println("onAuthenticationFailed");
                }
            });

            final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login")
                    .setSubtitle("Login using your biometric credential")
                    .setDescription("Touch the fingerprint sensor")
                    .setNegativeButtonText("Use pin code")
                    .build();

            biometricPrompt.authenticate(promptInfo);

            btnSetUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleContinueButton();
                }
            });


        }

    }

    private void handleContinueButton() {
        if (edtPIN != null && !(edtPIN.getText().toString().trim().length() == 4)) {
            edtPIN.setError("Please enter pin");
            edtPIN.requestFocus();
            return;
        }

        int pin = sharedPreferences.getInt("securityPin", 0);
        int enteredPin = Integer.parseInt(edtPIN.getText().toString());

        if (enteredPin == pin) {
            callMainActivity();
        } else {
            Toast.makeText(this, "Entered pin is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSetUpButton(){
        if (edtPIN != null && !(edtPIN.getText().toString().trim().length() == 4)){
            edtPIN.setError("Please enter 4 digit pin");
            edtPIN.requestFocus();
            return;
        }
        if (edtConfirmPIN != null && !(edtConfirmPIN.getText().toString().equalsIgnoreCase(edtPIN.getText().toString()))){
            edtConfirmPIN.setError("Pin and confirm pin must be same");
            edtConfirmPIN.requestFocus();
            return;
        }

        int pin = Integer.parseInt(edtPIN.getText().toString().trim());
        editor.putInt("securityPin", pin);
        editor.commit();

        callMainActivity();
    }

    private void callMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
