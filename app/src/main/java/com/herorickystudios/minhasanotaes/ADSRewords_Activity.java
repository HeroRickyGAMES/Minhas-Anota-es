package com.herorickystudios.minhasanotaes;

//Programado por HeroRickyGames

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.*;

public class ADSRewords_Activity extends AppCompatActivity {

    private final String TAG = ADSRewords_Activity.class.getSimpleName();
    private InterstitialAd interstitialAd;

    private boolean testMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adsrewords);


        AudienceNetworkAds.initialize(this);

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

    }
}