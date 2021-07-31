package com.AZEM.diamonduc2.initialActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.AZEM.diamonduc2.BuildConfig;
import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.dialogs.dailyReward;
import com.AZEM.diamonduc2.models.User;
import com.AZEM.diamonduc2.navigatorFragments.mainFragment;
import com.AZEM.diamonduc2.navigatorFragments.profile.profileFragment;
import com.AZEM.diamonduc2.navigatorFragments.luck;

import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.startapp.sdk.adsbase.StartAppAd;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String TAG = "Main";
    private Fragment selectedFragment = null;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;
    private FragmentManager fragmentManager;
    private int REQUEST_CODE = 1606;

    private TextView id,username,numberOfDiamonds2;
    private CircleImageView profileImage;

    private UserViewModel userViewModel;

    private String currentVersion, lastestVersion;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public int  videoDiamond, inviteQuantity;

    private AdView adView0;

//    private boolean currentDay;
//
//    private int hours;

    private static int i ,j, k, l, m = 0;

    private static Integer sharedCrowns;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        remoteConfig();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        progressBar=findViewById(R.id.mainProgressBar);
//        adView0=findViewById(R.id.adView10);
//        AdRequest request=new AdRequest.Builder().build();
//        adView0.loadAd(request);

        StartAppAd.showSplash(this,savedInstanceState);

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {

            }
        });

        new getLastesVersion().execute();
        FirebaseInAppMessaging.getInstance().triggerEvent("main");
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        initiateWidgets();
        FirebaseInAppMessaging.getInstance().triggerEvent("main");


        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        String today = year + "" + month + "" + day;
        SharedPreferences preferences = getSharedPreferences("userData", 0);
        boolean currentDay2 = preferences.getBoolean(today, false);
        if (!currentDay2) {
            userData();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(today, true);
            editor.apply();
        }

        if (auth.getCurrentUser() != null) {
            userDataRec();
        }

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                dailyRewardF();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();

        bottomNavigationView = findViewById(R.id.bottomNavigator);
        fragmentManager.beginTransaction().replace(R.id.mainContainer, new mainFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setUpNav(item);
                return true;
            }
        });
        remoteConfig();
    }

    private void inAppReview() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ReviewManager manager = ReviewManagerFactory.create(MainActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // We can get the ReviewInfo object
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                        flow.addOnCompleteListener(task1 -> {
                            // The flow has finished. The API does not indicate whether the user
                        });
                    } else {
                        // There was some problem, continue regardless of the result.
                    }
                });
            }
        });
        thread.run();


    }






    private void initiateWidgets() {
        id=findViewById(R.id.id);
        username=findViewById(R.id.username);
        numberOfDiamonds2=findViewById(R.id.numberOfDiamonds2);
        profileImage=findViewById(R.id.profileImage);
        currentUser = auth.getCurrentUser();
        fragmentManager = getSupportFragmentManager();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);
    }

    private void setUpNav(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.earn:
                selectedFragment = new mainFragment();
                break;
            case R.id.reward:
                selectedFragment = new luck();
                break;
            case R.id.profile:
                selectedFragment = new profileFragment();
        }
        if (selectedFragment != null) {
            fragmentManager.beginTransaction().replace(R.id.mainContainer, selectedFragment, "my tag").commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        i=0;
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, firstScreen.class));
            finish();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updateAlertDialoge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Crown UC");
        builder.setMessage("new Update is Available");
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private class getLastesVersion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                lastestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="
                        + getPackageName())
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>" +
                                "span:nth-child(2)>div:nth-child(1)" +
                                ">span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return lastestVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            currentVersion = BuildConfig.VERSION_NAME;
            if (lastestVersion != null) {
                float cv = Float.parseFloat(currentVersion);
                float lv = Float.parseFloat(lastestVersion);
                if (lv > cv) {
                    updateAlertDialoge();
                }
            }
        }
    }

    private void userData() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            int x = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (x == 0) {
                    User user1 = dataSnapshot.getValue(User.class);
                    Integer diamonds = user1.getDiamonds();
                    Integer invitediamonds = (Integer) user1.getInviteDiamond();
                    userViewModel.getUser(currentUser.getUid()).observe(MainActivity.this, new Observer<User>() {
                        int i = 0;
                        @Override
                        public void onChanged(User user) {
                            if (i == 0) {
                                Integer roomdiamonds = (Integer) user.getDiamonds();
                                if (roomdiamonds != null && diamonds != null && invitediamonds == 0) {
                                    if (roomdiamonds - diamonds > 0) {
                                        if (roomdiamonds - diamonds <= 1000) {
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("diamonds", roomdiamonds);
                                            reference.updateChildren(map);
                                        } else if (roomdiamonds - diamonds > 1000) {
                                            user.setDiamonds(diamonds);
                                            userViewModel.update(user);
                                        }
                                    } else if (diamonds - roomdiamonds > 0) {
                                        if (diamonds - roomdiamonds <= 5000) {
                                            user.setDiamonds(diamonds);
                                            userViewModel.update(user);
                                        } else if (diamonds - roomdiamonds > 5000) {
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("diamonds", roomdiamonds);
                                            reference.updateChildren(map);
                                        }
                                    }
//
                                } else if (roomdiamonds == null) {
                                    user.setDiamonds(diamonds);
                                    userViewModel.update(user);
                                } else if (roomdiamonds == null && diamonds == null) {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("diamonds", 400);
                                    reference.updateChildren(map);
                                    user.setDiamonds(400);
                                    userViewModel.update(user);
                                }
                                if (invitediamonds != 0) {
                                    Integer newC = roomdiamonds + invitediamonds;
                                    Integer newC2 = diamonds + invitediamonds;
//                                    Toast.makeText(MainActivity.this, "newC=" + newC + " ,newC2" + newC2, Toast.LENGTH_SHORT).show();
                                    if (newC.equals(newC2)) {
                                        user.setDiamonds(newC);
                                        userViewModel.update(user);
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("diamonds", newC);
                                        reference.updateChildren(map);
                                    }
                                    if (newC - newC2 > 0) {
                                        if (newC - newC2 <= 1000) {
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("diamonds", newC);
                                            reference.updateChildren(map);
                                        } else if (newC - newC2 > 1000) {
                                            user.setDiamonds(newC2);
                                            userViewModel.update(user);
                                        }
                                    } else if (newC2 - newC > 0) {
                                        if (newC2 - newC <= 5000) {
                                            user.setDiamonds(newC2);
                                            userViewModel.update(user);
                                        } else if (newC2 - newC > 5000) {
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("diamonds", newC);
                                            reference.updateChildren(map);
                                        }
                                    }
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("invitediamonds", 0);
                                    reference.updateChildren(map);
                                }
                                i++;
                            }
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    x++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void userDataRec() {
        SharedPreferences.Editor editor = getSharedPreferences("rewardsAdapter", 0).edit();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);
        userViewModel.getUser(currentUser.getUid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                id.setText(user.getUserID());
                username.setText(user.getUsername());
                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(user.getImgUrl())
                        .into(profileImage);
                numberOfDiamonds2.setText(String.valueOf(user.getDiamonds()));
                editor.putInt("crowns", user.getDiamonds());
                editor.apply();
            }
        });
    }

    private void dailyRewardF() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today = year + "" + month + "" + day;
        SharedPreferences preferences =getSharedPreferences("daily", 0);
        boolean currentDay = preferences.getBoolean(today, false);
        if (!currentDay) {
            dailyReward dailyReward1 = new dailyReward();
            FragmentManager manager = getSupportFragmentManager();

//            dailyReward1.onCancel(new DialogInterface() {
//                @Override
//                public void cancel() {
//                    vpnDialog();
//                }
//
//                @Override
//                public void dismiss() {
//                    vpnDialog();
//                }
//            });

            dailyReward1.show(manager, "dailyReward");

//            dailyReward1.onDismiss(new DialogInterface() {
//                @Override
//                public void cancel() {
//                    vpnDialog();
//                }
//
//                @Override
//                public void dismiss() {
//                    vpnDialog();
//                }
//            });

        }
    }

    public void vpnDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.vpn2);
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName ="com.AZEM.vpnCrown"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    public void remoteConfig() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(0)
                        .build();
                mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
                mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
                mFirebaseRemoteConfig.fetchAndActivate()
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Boolean> task) {
                                if (task.isSuccessful()) {
                                    boolean updated = task.getResult();
                                    Log.d(TAG, "Config params updated: " + updated);
                                    videoDiamond = (int) mFirebaseRemoteConfig.getLong("diamond_video");
                                    inviteQuantity = (int) mFirebaseRemoteConfig.getLong("invite_quantity");
                                    SharedPreferences preferences = getSharedPreferences("remote", MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putInt("videoDiamond",1);
                                    editor.putInt("invite_quantity",10);
                                    editor.apply();

                                } else {
                                    Toast.makeText(MainActivity.this, "Fetch failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        thread.run();
    }

}