package com.AZEM.diamonduc2.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.models.User;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.HashMap;
import java.util.UUID;

import static com.applovin.sdk.AppLovinSdkUtils.runOnUiThread;


public class insufficiencyDialog extends DialogFragment {
    private Button watchVideos;
    private String TAG = "insuff dialoge";
    private Button openTasks;
    private MaxInterstitialAd interstitialAd;

    private MaxRewardedAd rewardedAd;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private AdGem adGem;

    private RewardedAd admobrewardedAd;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private int videoDiamond=1;








    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insufficiency_dialog, container, false);
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);

        remoteConfig();
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        rewardeedAdmob();
        AppLovinSdk.getInstance(getActivity()).setMediationProvider("max");
        AppLovinSdk.initializeSdk(getActivity(), new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
            }
        });
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        applovinRewarde();
        adgem();
        watchVideos = view.findViewById(R.id.notEnoughWatchVideo);
        openTasks = view.findViewById(R.id.notEnoughSeeTasks);
        watchVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(admobrewardedAd!=null){
                admobrewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        userViewModel.getUser(currentUser.getUid()).observe(getActivity(), new Observer<User>() {
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
                        db.collection(currentUser.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        });
                    }
                });
            }else if(rewardedAd.isReady()){
                    rewardedAd.showAd();
                }else {
                    Toast.makeText(getActivity(), "your video is getting ready, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        openTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adGem.isOfferWallReady()) {
                    adGem.showOfferWall(getActivity());
                } else {
                    Toast.makeText(getActivity(), "please, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
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
                userViewModel.getUser(currentUser.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newD = diamonds + 1;
                            user.setDiamonds(newD);
                            userViewModel.update(user);
                            i++;
                        }
                    }

                });

//                numberOfWatched = numberOfWatched + amountRatio;
                String code = random();
                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 1 + " diamond earned from watching a diamond video with code: " + code;
                map1.put("details", transition);
                db.collection(currentUser.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
                StartAppAd.showAd(getActivity());

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


    private void adgem() {
        adGem = AdGem.get();
        OfferWallCallback callback = new OfferWallCallback() {
            @Override
            public void onOfferWallStateChanged(int newState) {
                // Notifies offer wall state change
            }

            @Override
            public void onOfferWallReward(int amount) {
                userViewModel.getUser(currentUser.getUid()).observe(getActivity(), new Observer<User>() {
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
                db.collection(currentUser.getUid()).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

}

