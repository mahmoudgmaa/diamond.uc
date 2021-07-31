package com.AZEM.diamonduc2.navigatorFragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.AZEM.diamonduc2.R;

import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.adapters.FragmentTabLayoutAdapter;
import com.AZEM.diamonduc2.dialogs.dailyReward;
import com.AZEM.diamonduc2.initialActivities.MainActivity;
import com.AZEM.diamonduc2.models.User;
import com.AZEM.diamonduc2.tabFragments.cod;
import com.AZEM.diamonduc2.tabFragments.freeFire;
import com.AZEM.diamonduc2.tabFragments.lords_mobile;
import com.AZEM.diamonduc2.tabFragments.mobile_legands;
import com.AZEM.diamonduc2.tabFragments.pubgUc;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adgem.android.AdGem;
import com.adgem.android.OfferWallCallback;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static com.applovin.sdk.AppLovinSdkUtils.runOnUiThread;


public class mainFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String TAG = "main fragment";
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private TabLayout mainTabLayout;
    private ViewPager viewPager;

    private boolean currentDay;

    private FragmentTabLayoutAdapter fragmentTabLayoutAdapter;
    private int hours;

    private TextView d1,d10;
    private int videoDiamond, inviteQuantity;
    private MaxRewardedAd rewardedAd;
    private FirebaseUser user1;
    private FirebaseFirestore db;

    private RelativeLayout watch,tasks,invite,offers;

    private AdColonyAdOptions adOptions;
    private String adColoneZoneId = "vzb433caa6d9af44dca7";
    private AdColonyInterstitial adColonyInterstitial;
    private AdColonyInterstitialListener listener;
    private Boolean isReady = false;
    private AdGem adGem;

    private Uri mInvitationUrl;
    private String invitationLink;
    private MaxInterstitialAd interstitialAd;

    private InterstitialAd mInterstitialAd;

    private RewardedAd admobrewardedAd;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private AlertDialog.Builder builder;
    private boolean appeared;
    private SharedPreferences preferences;
    private String today;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);

        AppLovinSdk.getInstance(getActivity()).setMediationProvider("max");
        AppLovinSdk.initializeSdk(getActivity(), new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
            }
        });

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        adMob();
        rewardeedAdmob();

        initiateWigets(view);
//        sharedpref();
        remoteConfig();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                applovinRewarde();
//                adColony1();
                adgem();
                applovin();
            }
        });
        thread.start();
        inviteFriends();
        viewPagerConfig(viewPager,mainTabLayout);
        tabLayoutConfig(mainTabLayout);
        vpnDialog();

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpnDialog();
                if (!appeared) {
                    builder.show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(today, true);
                    editor.apply();
                }else {
                    if (admobrewardedAd != null) {
                        Activity activityContext = getActivity();
                        admobrewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
                                userViewModel.getUser(user1.getUid()).observe(getActivity(), new Observer<User>() {
                                    int i = 0;

                                    @Override
                                    public void onChanged(User user) {
                                        if (i == 0) {
                                            int diamonds = user.getDiamonds();
                                            diamonds = diamonds + videoDiamond;
                                            user.setDiamonds(diamonds);
                                            userViewModel.update(user);
                                            i++;
                                        }
                                    }

                                });

//                numberOfWatched = numberOfWatched + amountRatio;
                                String code = random();
                                HashMap<String, Object> map1 = new HashMap<>();
                                String transition = videoDiamond + " diamonds earned from watching a video with code: " + code;
                                map1.put("details", transition);
                                db.collection(user1.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                });
                            }
                        });
                    } else if (rewardedAd.isReady()) {
                        rewardedAd.showAd();
                    } else if (isReady) {
                        adColonyInterstitial.show();
                    } else {
                        Toast.makeText(getActivity(), "your video is getting ready, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adGem.isOfferWallReady()) {
                    adGem.showOfferWall(getActivity());
                } else {
                    Toast.makeText(getActivity(), "please, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mInterstitialAd!=null){
//                    mInterstitialAd.show(getActivity());
//                }else if (interstitialAd.isReady()){
//                    interstitialAd.showAd();
//                }else {
//                    StartAppAd.showAd(getActivity());
//                }
                StartAppAd.showAd(getActivity());
                String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String subject = String.format("%s do you want to earn money?, lets try crown uc!", referrerName);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, invitationLink);
                intent.putExtra(intent.EXTRA_SUBJECT, subject);
                intent.setType("text/plain");
                if (mInvitationUrl == null) {
                    Toast.makeText(getActivity(), "your link is bieng generated, please try again!", Toast.LENGTH_SHORT).show();
                } else {
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), com.AZEM.diamonduc2.navigatorFragments.offers.class));
//                if(mInterstitialAd!=null){
//                    mInterstitialAd.show(getActivity());
//                }else if(interstitialAd.isReady()){
//                    interstitialAd.showAd();
//                }
            }
        });

//        sharedpref();
        return view;
    }

    private void inviteFriends() {
        String uid = user1.getUid();
        String link = "https://mygame.example.com/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://diamonduc.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = shortDynamicLink.getShortLink();
                        invitationLink = mInvitationUrl.toString();
                    }
                });
    }

    private void adgem() {
        adGem = AdGem.get();
        OfferWallCallback callback = new OfferWallCallback() {
            @Override
            public void onOfferWallStateChanged(int newState) {
                // Notifies offer wall state change
            }

            @Override
            public void onOfferWallReward(int amount) {
                userViewModel.getUser(user1.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newD = diamonds + amount;
                            user.setDiamonds(newD);
                            userViewModel.update(user);
                            i++;
                        }
                    }

                });
                String code = random();
                HashMap<String, Object> map1 = new HashMap<>();
                String transition = amount + " diamonds earned from doing a task with code: " + code;
                map1.put("details", transition);
                db.collection(user1.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });
            }

            @Override
            public void onOfferWallClosed() {

            }
        };
        adGem.registerOfferWallCallback(callback);
    }

    private void initiateWigets(View view) {
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user1=auth.getCurrentUser();
        d1=view.findViewById(R.id.d1);
        d10=view.findViewById(R.id.d10);
        currentUser=auth.getCurrentUser();
        mainTabLayout=view.findViewById(R.id.mainTabLayout);
        viewPager=view.findViewById(R.id.viewPager);
        watch=view.findViewById(R.id.watch);
        invite=view.findViewById(R.id.friends);
        tasks=view.findViewById(R.id.tasks);
        offers=view.findViewById(R.id.offers);
    }


        public void tabLayoutConfig (TabLayout tabLayout){
        tabLayout.getTabAt(0).setIcon(R.drawable.ppuubbgg);
            tabLayout.getTabAt(1).setIcon(R.drawable.cod);
            tabLayout.getTabAt(2).setIcon(R.drawable.freefire1);
            tabLayout.getTabAt(3).setIcon(R.drawable.mobilelegends);
            tabLayout.getTabAt(4).setIcon(R.drawable.lords);
        }
        public void viewPagerConfig (ViewPager viewPager, TabLayout tabLayout){
            fragmentTabLayoutAdapter = new FragmentTabLayoutAdapter(getChildFragmentManager());
            fragmentTabLayoutAdapter.addFragment(new pubgUc());
            fragmentTabLayoutAdapter.addFragment(new cod());
            fragmentTabLayoutAdapter.addFragment(new freeFire());
            fragmentTabLayoutAdapter.addFragment(new mobile_legands());
            fragmentTabLayoutAdapter.addFragment(new lords_mobile());
            viewPager.setAdapter(fragmentTabLayoutAdapter);
            tabLayout.setupWithViewPager(viewPager);

        }

    private void sharedpref() {
        SharedPreferences preferences = getActivity().getSharedPreferences("remote", MODE_PRIVATE);
        videoDiamond= preferences.getInt("videoDiamond", 1);
        inviteQuantity = preferences.getInt("invite_quantity", 10);
        d1.setText(videoDiamond+"");
        d10.setText(inviteQuantity+"");

    }
    private void applovinRewarde() {
        rewardedAd = MaxRewardedAd.getInstance("015feb21e9f24f1a", getActivity());
        rewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onRewardedVideoStarted(MaxAd ad) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {
                rewardedAd.loadAd();
            }

            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                userViewModel.getUser(user1.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newD = diamonds + videoDiamond;
                            user.setDiamonds(newD);
                            userViewModel.update(user);
                            i++;
                        }
                    }

                });

//                numberOfWatched = numberOfWatched + amountRatio;
                String code = random();
                HashMap<String, Object> map1 = new HashMap<>();
                String transition = videoDiamond + " diamonds earned from watching a video with code: " + code;
                map1.put("details", transition);
                db.collection(user1.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

            }

            @Override
            public void onAdLoaded(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, int errorCode) {
                rewardedAd.loadAd();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                rewardedAd.loadAd();
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                rewardedAd.loadAd();
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, int errorCode) {
                rewardedAd.loadAd();
            }
        });

        rewardedAd.loadAd();
    }
    public static String random() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

//    public void adColony1(){
//        AdColonyAppOptions appOptions = new AdColonyAppOptions()
//                .setUserID(user1.getUid())
//                .setGDPRRequired(true)
//                .setKeepScreenOn(true);
//        AdColony.configure(getActivity(), appOptions, "app39d962dbea5a4e0d9e", adColoneZoneId);
//        adOptions = new AdColonyAdOptions();
//        AdColony.setRewardListener(new AdColonyRewardListener() {
//            @Override
//            public void onReward(AdColonyReward reward) {
//                // Query reward object for info here
//                int rewardAmount = reward.getRewardAmount();
//                userViewModel.getUser(user1.getUid()).observe(getActivity(), new Observer<User>() {
//                    int i = 0;
//
//                    @Override
//                    public void onChanged(User user) {
//                        if (i == 0) {
//                            int diamonds = user.getDiamonds();
//                            int newD = diamonds + videoDiamond;
//                            user.setDiamonds(newD);
//                            userViewModel.update(user);
//                            i++;
//                        }
//                    }
//
//                });
//
////                numberOfWatched = numberOfWatched + amountRatio;
//                String code = random();
//                HashMap<String, Object> map1 = new HashMap<>();
//                String transition = videoDiamond + " diamonds earned from watching a video with code: " + code;
//                map1.put("details", transition);
//                db.collection(user1.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                });
//
//
//            }
//        });
//        listener = new AdColonyInterstitialListener() {
//            @Override
//            public void onRequestFilled(AdColonyInterstitial ad) {
//                /** Store and use this ad object to show your ad when appropriate */
//                adColonyInterstitial = ad;
//                isReady = true;
//            }
//
//
//            @Override
//            public void onExpiring(AdColonyInterstitial ad) {
//                super.onExpiring(ad);
//                AdColony.requestInterstitial(adColoneZoneId, this, adOptions);
//                AdColony.requestInterstitial("vz07380248623d47568a", this, adOptions);
//
//
//            }
//        };
//        AdColony.requestInterstitial(adColoneZoneId, listener);
//        AdColony.requestInterstitial("vz07380248623d47568a", listener);
//
//    }

    private void applovin() {
//                adView0.loadAd();
//                adView0.setVisibility(View.VISIBLE);
//                adView0.startAutoRefresh();
                interstitialAd = new MaxInterstitialAd("2713e0f7b6a76771", getActivity());
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

    public void rewardeedAdmob() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(getActivity(), "ca-app-pub-6437456633370907/9015700266",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        admobrewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        admobrewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad");
                    }
                });
        if (admobrewardedAd != null) {
            admobrewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad was shown.");
                    RewardedAd.load(getActivity(), "ca-app-pub-6437456633370907/9015700266",
                            adRequest, new RewardedAdLoadCallback() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error.
                                    Log.d(TAG, loadAdError.getMessage());
                                    admobrewardedAd = null;
                                }

                                @Override
                                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                    admobrewardedAd = rewardedAd;
                                    Log.d(TAG, "onAdFailedToLoad");
                                }
                            });
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.d(TAG, "Ad failed to show.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    Log.d(TAG, "Ad was dismissed.");
                }
            });
        }
    }


    public void adMob() {
        AdRequest adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                com.google.android.gms.ads.interstitial.InterstitialAd.load(getActivity(), "ca-app-pub-6437456633370907/7720233800", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
            }
        });

        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.
                    Log.d("TAG", "The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    Log.d("TAG", "The ad was shown.");
                    com.google.android.gms.ads.interstitial.InterstitialAd.load(getActivity(), "ca-app-pub-6437456633370907/7720233800", adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            com.google.android.gms.ads.interstitial.InterstitialAd.load(getActivity(), "ca-app-pub-6437456633370907/7720233800", adRequest, new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    mInterstitialAd = interstitialAd;

                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    mInterstitialAd = null;
                                }
                            });
                        }
                    });
                }
            });
        }
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
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Boolean> task) {
                                if (task.isSuccessful()) {
                                    boolean updated = task.getResult();
                                    Log.d(TAG, "Config params updated: " + updated);
                                    videoDiamond = (int) mFirebaseRemoteConfig.getLong("diamond_video");
                                    inviteQuantity = (int) mFirebaseRemoteConfig.getLong("invite_quantity");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            d1.setText(videoDiamond+"");
                                            d10.setText(inviteQuantity+"");

                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Fetch failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        thread.run();
    }

    @Override
    public void onResume() {
        super.onResume();
        adMob();
        rewardeedAdmob();
    }

    private void vpnDialog() {
        preferences = getActivity().getSharedPreferences("vpn", 0);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        today = year + "" + month + "" + day;
        appeared = preferences.getBoolean(today, false);
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.vpn2);
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = "com.AZEM.vpnCrown"; // getPackageName() from Context or Activity object
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
    }

}