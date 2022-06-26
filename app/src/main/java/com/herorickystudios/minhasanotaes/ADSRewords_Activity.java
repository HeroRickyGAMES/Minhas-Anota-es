package com.herorickystudios.minhasanotaes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unity3d.ads.IUnityAdsListener;
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

        UnityAds.initialize(this, GameID, testMode);

        IUnityAdsListener rewardedListner = new IUnityAdsListener() {
            @Override
            public void onUnityAdsReady(String s) {
                if(testMode == true){
                    Toast.makeText(ADSRewords_Activity.this, "Está pronto!", Toast.LENGTH_SHORT).show();
                }
                if(UnityAds.isReady()){

                    if(testMode == true){
                        Toast.makeText(ADSRewords_Activity.this, "Executou!", Toast.LENGTH_SHORT).show();
                    }

                    UnityAds.show(ADSRewords_Activity.this, rewardedPlacement);
                }
            }

            @Override
            public void onUnityAdsStart(String s) {

            }

            @Override
            public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
                // Implement conditional logic for each ad completion status:

                System.out.println(finishState);
                String Verifica = String.valueOf(finishState);
                String completo = "COMPLETED";

                System.out.println(Verifica);

                if (Verifica == completo) {
                    // Reward the user for watching the ad to completion.

                    if(testMode == true){
                        Toast.makeText(ADSRewords_Activity.this, "Completed", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(ADSRewords_Activity.this, MainActivity.class);
                    startActivity(intent);

                    onDestroy();


                } else if (finishState == UnityAds.FinishState.SKIPPED) {
                    // Do not reward the user for skipping the ad.
                    Toast.makeText(ADSRewords_Activity.this, "Skipped", Toast.LENGTH_SHORT).show();
                } else if (finishState == UnityAds.FinishState.ERROR) {
                    // Log an error.
                    Toast.makeText(ADSRewords_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
                Toast.makeText(ADSRewords_Activity.this,"Error "+unityAdsError,Toast.LENGTH_SHORT).show();
            }
        };

        if (UnityAds.isReady(rewardedPlacement)) {
            Toast.makeText(this, "Ad", Toast.LENGTH_SHORT).show();
            UnityAds.show(ADSRewords_Activity.this, rewardedPlacement);
        }
        UnityAds.setListener(rewardedListner);
        UnityAds.load(interPlacement);

    }
}