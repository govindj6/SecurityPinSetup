package com.example.securitypinsetup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;

    private FingerprintListener fingerprintListener;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject, FingerprintListener fingerprintListener) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        this.fingerprintListener = fingerprintListener;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success) {
        if (success) {
            if (fingerprintListener != null) {
                fingerprintListener.onSuccessfullyScan(e);
            }
        } else {
            Toast.makeText(context, "Not match", Toast.LENGTH_SHORT).show();
        }
    }

    public interface FingerprintListener {
        void onSuccessfullyScan(String s);
    }
}
