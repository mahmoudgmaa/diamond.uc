package com.AZEM.diamonduc2.navigatorFragments.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.adapters.historyAdapter;
import com.AZEM.diamonduc2.models.history;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;

public class historyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private historyAdapter adapter;
    private ArrayList<history> historyArrayList;
    private FirebaseAuth auth;
    private FirebaseUser user1;
    private String userId;
    private Toolbar historyToolbar;
    private FirebaseFirestore db;
    private String TAG = "history";
    private MaxAdView adView0;

    private AdView AdV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
//        AdV=findViewById(R.id.adView78);
//        AdRequest request=new AdRequest.Builder().build();
//        AdV.loadAd(request);


        StartAppAd.showAd(this);

        adView0=findViewById(R.id.adViewHistory);
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
                adView0.loadAd();
                adView0.setVisibility( View.VISIBLE );
                adView0.startAutoRefresh();
            }
        } );

        auth = FirebaseAuth.getInstance();
        user1 = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        historyToolbar = findViewById(R.id.historyToolbar);
        setSupportActionBar(historyToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        historyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        user1 = auth.getCurrentUser();
        userId = user1.getUid();
        recyclerView = findViewById(R.id.historyRec);
        historyArrayList = new ArrayList<>();
        readHistory();
        adapter = new historyAdapter(historyArrayList, historyActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void readHistory() {
        db.collection(userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    historyArrayList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        history history1 = document.toObject(history.class);
                        historyArrayList.add(history1);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}