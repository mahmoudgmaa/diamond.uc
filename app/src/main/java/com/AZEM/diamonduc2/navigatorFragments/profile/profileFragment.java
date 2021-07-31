package com.AZEM.diamonduc2.navigatorFragments.profile;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.initialActivities.MainActivity;
import com.AZEM.diamonduc2.models.User;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startapp.sdk.adsbase.StartAppAd;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {
    private Button edit, history1, requests,download;
    private RelativeLayout fac, ins, gm;
    private AlertDialog.Builder dialog;
    private MaxInterstitialAd interstitialAd;
    private UserViewModel userViewModel;
    private FirebaseUser currentUser;
    private InterstitialAd mInterstitialAd;


    @Override
    public void onResume() {
        super.onResume();
        adMob();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initiateWidgets(view);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        StartAppAd.showAd(getActivity());
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        adMob();
        AppLovinSdk.getInstance(getActivity()).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( getActivity(), new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {

            }
        } );
        applovin();
        String UID = "complaint from " + currentUser.getUid();

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), editProfile.class));
//                if(mInterstitialAd!=null){
//                    mInterstitialAd.show(getActivity());
//                }else if (interstitialAd.isReady()){
//                    interstitialAd.showAd();
//                }else {
//                    StartAppAd.showAd(getActivity());
//                }
            }
        });
        history1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), historyActivity.class));
//                if(mInterstitialAd!=null){
//                    mInterstitialAd.show(getActivity());
//                }else if (interstitialAd.isReady()){
//                    interstitialAd.showAd();
//                }else {
//                    StartAppAd.showAd(getActivity());
//                }
            }
        });
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Requests.class));
//                if(mInterstitialAd!=null){
//                    mInterstitialAd.show(getActivity());
//                }else if (interstitialAd.isReady()) {
//                    interstitialAd.showAd();
//                } else {
//                    StartAppAd.showAd(getActivity());
//                }
            }

        });

        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/crown._.uc");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/crown._.uc/")));
                }
            }
        });
        fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://web.facebook.com/groups/1028937614230301/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.facebook.katana");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://web.facebook.com/groups/1028937614230301/")));
                }
            }
        });

        gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"diamond.uc1999@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, UID);

                startActivity(Intent.createChooser(intent, "Email via..."));
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName ="com.AZEM.crownuc"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        return view;
    }

    private void initiateWidgets(View view) {

        edit = view.findViewById(R.id.editYourProfile);
        requests = view.findViewById(R.id.requests);
        fac = view.findViewById(R.id.rrrr);
        ins = view.findViewById(R.id.r2r2);
        gm = view.findViewById(R.id.r3r3);
        history1 = view.findViewById(R.id.history);
        download=view.findViewById(R.id.download);
    }

    private void applovin() {
        // AppLovin SDK is initialized, start loading ads
        interstitialAd = new MaxInterstitialAd( "2713e0f7b6a76771",getActivity());
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


}