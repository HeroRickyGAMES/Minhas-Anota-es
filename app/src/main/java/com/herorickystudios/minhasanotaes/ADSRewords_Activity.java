package com.herorickystudios.minhasanotaes;

//Programado por HeroRickyGames

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;


public class ADSRewords_Activity extends AppCompatActivity {

    private String GameID = "4814302";
    private String interAD = "Interstitial_Android";
    private String bannerPlacement = "Banner_Android";
    private String interPlacement = "Interstitial_Android";
    private String rewardedPlacement="Rewarded_Android";
    private boolean testMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adsrewords);


        IUnityAdsInitializationListener initializationListener = new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                if(testMode == true) {
                    Toast.makeText(ADSRewords_Activity.this, "Iniciou!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
                Toast.makeText(ADSRewords_Activity.this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            }
        };

        UnityAds.initialize(this, GameID, testMode, initializationListener);



        IUnityAdsShowListener unityAdsShowListener = new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                Toast.makeText(ADSRewords_Activity.this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityAdsShowStart(String s) {
                if(testMode == true) {
                    Toast.makeText(ADSRewords_Activity.this, "Start!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityAdsShowClick(String s) {

            }

            @Override
            public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                Intent intent = new Intent(ADSRewords_Activity.this, MainActivity.class);
                startActivity(intent);
                    if(testMode == true) {
                        Toast.makeText(ADSRewords_Activity.this, "Completo!", Toast.LENGTH_SHORT).show();
                    }
            }
        };

        IUnityAdsLoadListener adsLoadListener = new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                if(testMode == true) {
                    Toast.makeText(ADSRewords_Activity.this, "Iniciado!", Toast.LENGTH_SHORT).show();
                }
                UnityAds.show(ADSRewords_Activity.this, interPlacement, unityAdsShowListener);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {

            }
        };
        UnityAds.load(interPlacement, adsLoadListener);
    }
}