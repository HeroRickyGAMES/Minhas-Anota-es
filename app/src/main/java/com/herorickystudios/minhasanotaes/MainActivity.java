package com.herorickystudios.minhasanotaes;

//Programado por HeroRickyGames

import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.herorickystudios.minhasanotaes.databinding.ActivityMainBinding;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.facebook.ads.*;
import java.net.NetworkInterface;

public class MainActivity extends AppCompatActivity {

    Boolean testMode = true;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private AdView adView;
    //private AnotacaoPreferencias preferencias;
    private EditText editAnotacao;

    private final String TAG = ADSRewords_Activity.class.getSimpleName();
    private InterstitialAd interstitialAd;

    private FirebaseDatabase referencia = FirebaseDatabase.getInstance();

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    //Data base
    private FirebaseDatabase database;

    //referencia para o usuario
    private DatabaseReference referenciaP = FirebaseDatabase.getInstance().getReference("Usuarios");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editAnotacao = findViewById(R.id.editAnotacao);

        AudienceNetworkAds.initialize(this);

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#326901805789557_561404239005978", AdSize.BANNER_HEIGHT_50);

        interstitialAd = new InterstitialAd(this, "326901805789557_561568765656192");

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
// Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build()
        );

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Toast.makeText(
                                MainActivity.this,
                                "Error: " + adError.getErrorMessage(),
                                Toast.LENGTH_LONG)
                        .show();
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


        //preferencias = new AnotacaoPreferencias(getApplication());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoRecuperado = editAnotacao.getText().toString();

                if (textoRecuperado.equals("")) {
                    Snackbar.make(view, "Preencha a anotação!", Snackbar.LENGTH_LONG).show();

                } else {

                    //preferencias.salvarAnotacao( textoRecuperado );

                    Snackbar.make(view, "Anotação Salva com sucesso!", Snackbar.LENGTH_LONG).show();

                    editAnotacao.getText();

                    editAnotacao.setText(textoRecuperado);

                    String uid = usuario.getUid();

                    referenciaP.child(uid).child("anotacao").setValue(textoRecuperado);

                }
            }
        });


        //Database instruções
        DatabaseReference reference = referencia.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String uid = usuario.getUid();

                Log.i("FIREBASE", snapshot.getValue().toString());
                String anotacao = snapshot.child("Usuarios").child(uid).child("anotacao").getValue().toString();

                editAnotacao.setText(anotacao);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("FIREBASE", "Ocorreu um erro ao acessar o banco de dados da aplicação, certifique-se se está tudo certo com o codigo ou a internet" + error);
            }
        });

        //Recuperar Anotação
        //String anotacao =  preferencias.recuperarAnotacao();
        /*String anotacao =  "testee";
        if( !anotacao.equals("") ){
            editAnotacao.setText(anotacao);
        }*/

        checkinternet();

    }

    public void checkinternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            if (testMode == true) {
                System.out.println("Está online!");
            }
        } else {
            if (testMode == true) {
                System.out.println("Está Offline!");
            }
            Toast.makeText(this, "Você está offline! Por favor, verifique sua conexão com a rede e tente novamente!", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, Autenticacao_activity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        //Quando o aplicativo entra em segundo plano ele executa essa função!
        String textoRecuperado = editAnotacao.getText().toString();

        if(textoRecuperado.equals(""))

        {
            Toast.makeText(this, "Anotação vazia!", Toast.LENGTH_SHORT).show();

        }else

        {

            Toast.makeText(this, "Anotação Salva com sucesso enquanto o app fica em segundo plano!", Toast.LENGTH_SHORT).show();

            editAnotacao.getText();

            editAnotacao.setText(textoRecuperado);

            String uid = usuario.getUid();

            referenciaP.child(uid).child("anotacao").setValue(textoRecuperado);

            super.onPause();

        }
    }

    public void serverStatusbtn(View view){
        Intent intent = new Intent(this, ServerStatus_Activity.class);
        startActivity(intent);
    }
}