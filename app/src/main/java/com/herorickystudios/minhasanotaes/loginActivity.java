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
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

public class loginActivity extends AppCompatActivity {

    private String GameID = "4814302";
    private String interAD = "Interstitial_Android";
    private String bannerPlacement = "Banner_Android";
    private String interPlacement = "Interstitial_Android";
    private String rewardedPlacement="Rewarded_Android";
    private boolean testMode = false;

    private TextView editEmaillg, editSenhalg;

    String[] menssagens = {"Preencha todos os campos para continuar", "Login feito com sucesso!"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmaillg = findViewById(R.id.editEmaillg);
        editSenhalg = findViewById(R.id.editSenhalg);

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
                    Toast.makeText(loginActivity.this, "Não carregou!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerShow(String s) {
                if(testMode == true){
                    Toast.makeText(loginActivity.this, "Apareceu o banner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerClick(String s) {

            }

            @Override
            public void onUnityBannerHide(String s) {
                Toast.makeText(loginActivity.this, "Está escondido!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityBannerError(String s) {
                if(testMode == true){
                    Toast.makeText(loginActivity.this, "Ocorreu um erro...", Toast.LENGTH_SHORT).show();
                }
            }
        };

        UnityBanners.setBannerListener(bannerListener);

        IUnityAdsLoadListener adsLoadListener = new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                if(testMode == true) {
                    Toast.makeText(loginActivity.this, "Iniciado!", Toast.LENGTH_SHORT).show();
                }

                UnityBanners.loadBanner(loginActivity.this, bannerPlacement);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {

            }
        };
        UnityAds.load(rewardedPlacement, adsLoadListener);

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