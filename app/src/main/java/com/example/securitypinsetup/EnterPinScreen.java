package com.example.securitypinsetup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EnterPinScreen extends AppCompatActivity {

    private EditText edtPIN;
    private Button btnContinue;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pin_screen);

        edtPIN = findViewById(R.id.edt_pin);
        btnContinue = findViewById(R.id.btn_Continue);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleContinueButton();
            }
        });

    }

    private void handleContinueButton(){
        if (edtPIN != null && !(edtPIN.getText().toString().trim().length() == 4)){
            edtPIN.setError("Please enter pin");
            edtPIN.requestFocus();
            return;
        }

        int pin = sharedPreferences.getInt("securityPin",0);
        int enteredPin = Integer.parseInt(edtPIN.getText().toString());

        if (enteredPin == pin){
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Entered pin is incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}
