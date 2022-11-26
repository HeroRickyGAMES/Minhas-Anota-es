package com.herorickystudios.minhasanotaes;


import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import androidx.core.content.ContextCompat;

import android.content.Intent;
import androidx.biometric.BiometricPrompt;

import android.os.Bundle;
import android.provider.Settings;
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
                //notificarUsuario(errString.toString());

                System.out.println(errString);

                if(errString.toString().equals(errString.toString())){

                    Intent intent1 = new Intent(Settings.ACTION_SECURITY_SETTINGS);

                    Toast.makeText(Autenticacao_activity.this, "Você não tem uma tela de bloqueio!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Autenticacao_activity.this, "Você precisa colocar uma para usar!", Toast.LENGTH_SHORT).show();

                    startActivityForResult(intent1, 0);

                }
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                //notificarUsuario("Autenticação Aprovada!");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //notificarUsuario("Autenticação falhou!");
            }
        };
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }
    private void notificarUsuario(String menssage){
        Toast.makeText(this, menssage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Use sua digital ou padrão!")
                .setSubtitle("Para entrar no aplicativo e ver as anotações")
                // Can't call setNegativeButtonText() and
                // setAllowedAuthenticators(...|DEVICE_CREDENTIAL) at the same time.
                // .setNegativeButtonText("Use account password")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
        getPrompt().authenticate(promptInfo);

        super.onResume();
    }
}