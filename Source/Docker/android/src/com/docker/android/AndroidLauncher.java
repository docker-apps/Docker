package com.docker.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.docker.AdController;
import com.docker.Docker;
import com.docker.domain.user.Inventory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.widget.RelativeLayout.LayoutParams;

import com.google.android.gms.ads.InterstitialAd;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;

public class AndroidLauncher extends AndroidApplication implements AdController {
	// Debug tag, for logging
	public static final String TAG = "Docker";
    private AdView adView;
    private InterstitialAd interstitialAd;
	private Inventory userInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        adView = createAdView();
        layout.addView(adView);
        layout.addView(createGameView(config));
        setContentView(layout);
        adView.loadAd(new AdRequest.Builder().build());
        setupInterstitial();
    }

    private View createGameView(AndroidApplicationConfiguration cfg) {
    	// In-App Billing (mostly straight from http://developer.android.com/training/in-app-billing/preparing-iab-app.html)
		// TODO: Obfuscate string according to http://developer.android.com/training/in-app-billing/preparing-iab-app.html#Connect
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn9tZy9cm10LKEnZrS29EcG9j2SKAqn6+QPmQuAwHsj+rc53rdO4eGfNFQFXuet7X+bmpCjZvyazzzNxQhyTuqqM8xmezo/rpfBHatzhRmg7pPJgrW5LpmyjReM/1Qbaw0Ib13W8A1U6O4CCngzihFmM2t/LWy+IFahU0x2DfY8bYtuR+mmPjBJ6Qu6XCiSBU8fCrfTgvP3vk3oKZFOyiUtSnlIfuBpKmFKhccFCpkQmmleGjwbux2dfWrl71hZtEfDfaaWRorNliXANX2qkkOMyN5+5BUoClCio5pnDhd1lgZzQO5sjHvVK1pLh6bpnrSRFC89oxwNoSQ11i1dXpPwIDAQAB";
		userInventory = new Inventory(this, base64EncodedPublicKey); 
		
        View gameView = initializeForView(new Docker(this, userInventory), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
    }

    private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
        adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
    }

    private void setupInterstitial() {

        AdBuddiz.setPublisherKey("f0655068-7dd9-4d63-8975-14b483b98b57");
        AdBuddiz.cacheAds(this);

        /*interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.ad_unit_interstitial_id));

        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest ad = builder.build();
        interstitialAd.loadAd(ad);*/
    }

    @Override
    public void showAds(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    adView.setVisibility(View.VISIBLE);
                } else {
                    adView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void showInterstitialAd() {
        final AndroidLauncher androidLauncher = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdBuddiz.showAd(androidLauncher);
                /*interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        AdRequest.Builder builder = new AdRequest.Builder();
                        AdRequest ad = builder.build();
                        interstitialAd.loadAd(ad);
                    }
                });
                interstitialAd.show();*/
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
		userInventory.dispose();
    }

    /**
     * Ist nötig, damit die Callback für das Billing funktionieren.
     */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
	
	    // Pass on the activity result to the helper for handling
	    if (!((Inventory)userInventory).getmHelper().handleActivityResult(requestCode, resultCode, data)) {
	        // not handled, so handle it ourselves (here's where you'd
	        // perform any handling of activity results not related to in-app
	        // billing...
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	    else {
	        Log.d(TAG, "onActivityResult handled by IABUtil.");
	    }
    }
}
