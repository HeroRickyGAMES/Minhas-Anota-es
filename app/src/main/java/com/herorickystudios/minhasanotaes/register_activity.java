package com.herorickystudios.minhasanotaes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

import java.util.concurrent.Executor;

public class register_activity extends AppCompatActivity {

    private String GameID = "4814302";
    private String interAD = "Interstitial_Android";
    private String bannerPlacement = "Banner_Android";
    private String interPlacement = "Interstitial_Android";
    private String rewardedPlacement="Rewarded_Android";
    private boolean testMode = false;

    String[] menssagens = {"Preencha todos os campos para continuar", "Cadastro feito com sucesso!"};

    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Usuarios");

    private EditText editNome, editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

        checkinternet();

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
                    Toast.makeText(register_activity.this, "N??o carregou!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerShow(String s) {
                if(testMode == true){
                    Toast.makeText(register_activity.this, "Apareceu o banner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerClick(String s) {

            }

            @Override
            public void onUnityBannerHide(String s) {
                if(testMode == true){
                Toast.makeText(register_activity.this, "Est?? escondido!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityBannerError(String s) {
                if(testMode == true){
                    Toast.makeText(register_activity.this, "Ocorreu um erro...", Toast.LENGTH_SHORT).show();
                }
            }
        };

        UnityBanners.setBannerListener(bannerListener);

        IUnityAdsLoadListener adsLoadListener = new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                if(testMode == true) {
                    Toast.makeText(register_activity.this, "Iniciado!", Toast.LENGTH_SHORT).show();
                }
                UnityBanners.loadBanner(register_activity.this, bannerPlacement);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {

            }
        };
        UnityAds.load(rewardedPlacement, adsLoadListener);

    }

    public void registerbtn(View view){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Snackbar snackbar = Snackbar.make(view, menssagens[1],Snackbar.LENGTH_LONG);
                    snackbar.show();

                    FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                    String getUID = usuarioLogado.getUid();

                    String nome = editNome.getText().toString();
                    String email = editEmail.getText().toString();
                    String anotacaouser = "";

                    referencia.child(getUID).child("username").setValue(nome);
                    referencia.child(getUID).child("email").setValue(email);
                    referencia.child(getUID).child("anotacao").setValue(anotacaouser);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);


                }else{
                    String erro;
                    try {

                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no m??nimo 6 caracteres!";
                    }catch (FirebaseAuthUserCollisionException e) {

                        erro = "Essa conta j?? foi criada, crie uma nova conta ou clique em esqueci minha senha na tela de login!";
                    }catch (FirebaseAuthInvalidCredentialsException e){

                        erro = "Seu email est?? digitado errado, verifique novamente!";

                    }catch (Exception e){
                        erro = "Ao cadastrar usu??rio!";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

            }
        });
    }

    public void checkinternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            System.out.println("Est?? online!");

        }else{
            System.out.println("Est?? Offline!");

            Toast.makeText(this, "Voc?? est?? offline! Por favor, verifique sua conex??o com a rede e tente novamente!", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }
}