package com.example.securitypinsetup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PinSetupScreen extends AppCompatActivity {

    private EditText edtPIN;
    private EditText edtConfirmPIN;
    private Button btnSetUp;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_setup_screen);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edtPIN = findViewById(R.id.edt_pin);
        edtConfirmPIN = findViewById(R.id.edt_confirm_pin);
        btnSetUp = findViewById(R.id.btn_set_pin);

        btnSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSetUpButton();
            }
        });

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

        Intent intent = new Intent(this,EnterPinScreen.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int pin =sharedPreferences.getInt("securityPin",0);
        if (pin != 0){
            Intent intent = new Intent(this,EnterPinScreen.class);
            startActivity(intent);
        }
    }
}
