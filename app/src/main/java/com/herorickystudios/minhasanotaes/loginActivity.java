package com.herorickystudios.minhasanotaes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.ads.*;

public class loginActivity extends AppCompatActivity {

    private AdView adView;

    private boolean testMode = true;

    private TextView editEmaillg, editSenhalg;

    String[] menssagens = {"Preencha todos os campos para continuar", "Login feito com sucesso!"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmaillg = findViewById(R.id.editEmaillg);
        editSenhalg = findViewById(R.id.editSenhalg);

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#326901805789557_561404239005978", AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);

// Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());

        checkinternet();
    }

    public void btnlogin(View view){
        String email = editEmaillg.getText().toString();
        String senha = editSenhalg.getText().toString();

        if( email.isEmpty() || senha.isEmpty() ){
            Snackbar snackbar = Snackbar.make(view, menssagens[0],Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            AutenticarUsuario(view);
        }
    }

    private void AutenticarUsuario(View view) {
        String email = editEmaillg.getText().toString();
        String senha = editSenhalg.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginActivity.this, "Logado com Sucesso", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), Autenticacao_activity.class);
                            startActivity(intent);

                }
            }
        });
    }

    public void btnregister(View view){

        Intent intent = new Intent(this, register_activity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioLogado != null){

            Intent intent = new Intent(getApplicationContext(), Autenticacao_activity.class);
            startActivity(intent);

        }

    }

    public void checkinternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            System.out.println("Está online!");

        }else{
            System.out.println("Está Offline!");

            Toast.makeText(this, "Você está offline! Por favor, verifique sua conexão com a rede e tente novamente!", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }

}