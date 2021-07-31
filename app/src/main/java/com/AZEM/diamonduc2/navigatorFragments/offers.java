package com.AZEM.diamonduc2.navigatorFragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.adapters.rewardAdapter;
import com.AZEM.diamonduc2.models.mainModel;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;

public class offers extends AppCompatActivity {
    private Toolbar offersToolBar;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private MaxAdView adView0;
    private MaxInterstitialAd interstitialAd;
    private StartAppAd startAppAd;

    private ArrayList<mainModel> rewardayaArrayList;
    private rewardAdapter rewardAdapter1;
    private RecyclerView recyclerView;

    private AdView AdV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        adView0 = findViewById(R.id.adView111);
        offersToolBar = findViewById(R.id.offersToolBar);
        applovin();
        startAppAd = new StartAppAd(this);
        startAppAd.loadAd(StartAppAd.AdMode.FULLPAGE);
        StartAppAd.showAd(this);

//         if (interstitialAd.isReady()) {
//            interstitialAd.showAd();
//        }else {
//             StartAppAd.showAd(this);
//         }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
//        AdV=findViewById(R.id.adV);
//        AdRequest request=new AdRequest.Builder().build();
//        AdV.loadAd(request);

        setSupportActionBar(offersToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        offersToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        recyclerView = findViewById(R.id.rewardRec);
        rewardayaArrayList = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                readTasks();
            }
        });
        thread.start();
        rewardAdapter1 = new rewardAdapter(this, rewardayaArrayList);
        recyclerView.setAdapter(rewardAdapter1);
    }


    private void applovin() {
        AppLovinSdk.getInstance(offers.this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(offers.this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
                adView0.loadAd();
                adView0.setVisibility(View.VISIBLE);
                adView0.startAutoRefresh();

                interstitialAd = new MaxInterstitialAd("2713e0f7b6a76771", offers.this);
                interstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, int errorCode) {
                        interstitialAd.loadAd();

                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        interstitialAd.loadAd();
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        interstitialAd.loadAd();

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, int errorCode) {
                        interstitialAd.loadAd();

                    }
                });

                // Load the first ad
                interstitialAd.loadAd();
            }
        });
    }

    private void readTasks() {
        rewardayaArrayList.clear();
        mainModel mainModel1 = new mainModel("317 CP", "6000 D", 6000, "cod");
        mainModel mainModel2 = new mainModel("530 Diamonds", "8000 D", 8000, "freefire");
        mainModel mainModel3 = new mainModel("600 Diamonds", "8000 D", 8000, "lord");
        mainModel mainModel5 = new mainModel("1088 Diamonds", "30000 D", 30000, "freefire");
        mainModel mainModel4 = new mainModel("311 Diamonds", "7000 D", 7000, "legand");
        mainModel mainModel6 = new mainModel("1373 CP", "30000 D", 30000, "cod");
        rewardayaArrayList.add(mainModel1);
        rewardayaArrayList.add(mainModel2);
        rewardayaArrayList.add(mainModel3);
        rewardayaArrayList.add(mainModel5);
        rewardayaArrayList.add(mainModel4);
        rewardayaArrayList.add(mainModel6);
    }
    @Override
    public void onBackPressed() {
//        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}