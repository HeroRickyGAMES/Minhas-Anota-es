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
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.net.NetworkInterface;

public class MainActivity extends AppCompatActivity {


    private String GameID = "4814302";
    private String interAD = "Interstitial_Android";
    private String bannerPlacement = "Banner_Android";
    private String interPlacement = "Interstitial_Android";
    private String rewardedPlacement="Rewarded_Android";
    private boolean testMode = false;


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    //private AnotacaoPreferencias preferencias;
    private EditText editAnotacao;

    private FirebaseDatabase referencia = FirebaseDatabase.getInstance();

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    //Data base
    private FirebaseDatabase database;

    //referencia para o usuario
    private DatabaseReference referenciaP = FirebaseDatabase.getInstance().getReference("Usuarios");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UnityAds.initialize(this, GameID, testMode);

        IUnityBannerListener bannerListener = new IUnityBannerListener() {
            @Override
            public void onUnityBannerLoaded(String s, View view) {
                ((ViewGroup) findViewById(R.id.banner_ad)).removeView(view);
                ((ViewGroup) findViewById(R.id.banner_ad)).addView(view);
            }

            @Override
            public void onUnityBannerUnloaded(String s) {
                if(testMode == true){
                    Toast.makeText(MainActivity.this, "Não carregou!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerShow(String s) {
                if(testMode == true){
                    Toast.makeText(MainActivity.this, "Apareceu o banner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerClick(String s) {

            }

            @Override
            public void onUnityBannerHide(String s) {
                Toast.makeText(MainActivity.this, "Está escondido!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityBannerError(String s) {
                if(testMode == true){
                    Toast.makeText(MainActivity.this, "Ocorreu um erro...", Toast.LENGTH_SHORT).show();
                }
            }
        };

        UnityBanners.setBannerListener(bannerListener);

        IUnityAdsLoadListener adsLoadListener = new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                Toast.makeText(MainActivity.this, "Iniciado!", Toast.LENGTH_SHORT).show();
                UnityBanners.loadBanner(MainActivity.this, bannerPlacement);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {

            }
        };
        UnityAds.load(rewardedPlacement, adsLoadListener);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editAnotacao = findViewById(R.id.editAnotacao);


        //preferencias = new AnotacaoPreferencias(getApplication());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoRecuperado = editAnotacao.getText().toString();

                if( textoRecuperado.equals("") ){
                    Snackbar.make(view, "Preencha a anotação!", Snackbar.LENGTH_LONG).show();

                }else{

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

                UnityBanners.loadBanner(MainActivity.this, bannerPlacement);

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

    public void checkinternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            if(testMode == true) {
                System.out.println("Está online!");
            }
        }else{
            if(testMode == true) {
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
}