package com.AZEM.diamonduc2.navigatorFragments.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.RequestsViewModel;
import com.AZEM.diamonduc2.adapters.RequestsAdapter;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Requests extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestsAdapter adapter;
    private ArrayList<com.AZEM.diamonduc2.models.Requests> requestsArrayList;
    private FirebaseAuth auth;
    private FirebaseUser user1;
    private Toolbar requestsToolbar;
    private FirebaseFirestore db;
    private String TAG = "history";
    private MaxAdView adView0;
    private TextView noreq;
    private SharedPreferences preferences;
    //    private RequestsViewModel requestsViewModel;
    private RequestsViewModel requestsViewModel;
    private boolean currentDay;
    private String today;
    private AdView AdV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
//        AdV=findViewById(R.id.adView145);
//        AdRequest request=new AdRequest.Builder().build();
//        AdV.loadAd(request);

        StartAppAd.showAd(this);

        requestsViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RequestsViewModel.class);

        requestsToolbar = findViewById(R.id.requestsToolbar);
        setSupportActionBar(requestsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("requests", 0);
        requestsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adView0 = findViewById(R.id.adViewRequests);
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
                adView0.loadAd();
                adView0.setVisibility(View.VISIBLE);
                adView0.startAutoRefresh();
            }
        });
        noreq = findViewById(R.id.noreq);

        auth = FirebaseAuth.getInstance();
        user1 = auth.getCurrentUser();
//        requestsViewModel = new ViewModelProvider(this,
//                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RequestsViewModel.class);
        recyclerView = findViewById(R.id.requestsRec);
        recyclerView.setHasFixedSize(true);
        requestsArrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        today = year + "" + month + "" + day;
        currentDay = preferences.getBoolean(today + "requests", false);
        if (!currentDay) {
            readRequests();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestsAdapter(this);
        recyclerView.setAdapter(adapter);
        requestsViewModel.getAllRequests().observe(Requests.this, new Observer<List<com.AZEM.diamonduc2.models.Requests>>() {
            @Override
            public void onChanged(List<com.AZEM.diamonduc2.models.Requests> requests) {
                adapter.setRequests(requests);
                if (requests.size() == 0) {
                    noreq.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void readRequests() {
        requestsViewModel.deleteAllRequests();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("requests").child(user1.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    com.AZEM.diamonduc2.models.Requests request = dataSnapshot.getValue(com.AZEM.diamonduc2.models.Requests.class);
//                    requestsArrayList.add(request);
                    requestsViewModel.insertRequests(request);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(today + "requests", true);
                    editor.apply();
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}