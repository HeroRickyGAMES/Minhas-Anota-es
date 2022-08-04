package com.herorickystudios.minhasanotaes;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import java.util.concurrent.Executor;

public class Autenticacao_activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Use sua digital ou padrão!")
                .setSubtitle("Para entrar no aplicativo e ver as anotações")
                // Can't call setNegativeButtonText() and
                // setAllowedAuthenticators(...|DEVICE_CREDENTIAL) at the same time.
                // .setNegativeButtonText("Use account password")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
        getPrompt().authenticate(promptInfo);
    }

    private BiometricPrompt getPrompt(){
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notificarUsuario(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notificarUsuario("Autenticação Aprovada!");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notificarUsuario("Autenticação falhou!");
            }
        };
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }
    private void notificarUsuario(String menssage){
        Toast.makeText(this, menssage, Toast.LENGTH_SHORT).show();
    }
}