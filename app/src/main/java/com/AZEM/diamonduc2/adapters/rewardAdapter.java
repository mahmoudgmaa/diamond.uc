package com.AZEM.diamonduc2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.dialogs.codDialog;
import com.AZEM.diamonduc2.dialogs.dialog;
import com.AZEM.diamonduc2.dialogs.freeFireDialog;
import com.AZEM.diamonduc2.dialogs.insufficiencyDialog;
import com.AZEM.diamonduc2.dialogs.legand_dialoge;
import com.AZEM.diamonduc2.dialogs.lordsDialoge;

import com.AZEM.diamonduc2.initialActivities.MainActivity;
import com.AZEM.diamonduc2.models.mainModel;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
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

import java.util.ArrayList;

public class rewardAdapter extends RecyclerView.Adapter<com.AZEM.diamonduc2.adapters.rewardAdapter.viewHolder> {
    private Context mContext;
    private ArrayList<mainModel> rewardayaArrayList;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private Integer crowns;
    private String TAG = "rewardAdapter";
    private MaxInterstitialAd interstitialAd;
    private UserViewModel userViewModel;
    private Activity activity;

    private InterstitialAd mInterstitialAd;



    public rewardAdapter(Context mContext, ArrayList<mainModel> rewardayaArrayList) {
        this.mContext = mContext;
        this.rewardayaArrayList = rewardayaArrayList;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.reward_item,parent,false);
        viewHolder holder=new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        applovin();
        adMob();
        SharedPreferences preferences = mContext.getSharedPreferences("rewardsAdapter", 0);
        crowns = preferences.getInt("crowns", 100);
        mainModel model = rewardayaArrayList.get(position);
        holder.name.setText(model.getModelName());
        holder.rewardPrice.setText(model.getCostString());
        String type = model.getType();
        switch (type) {
            case "pubg":
                holder.imageView.setImageResource(R.drawable.ppuubbgg);
                break;
            case "cod":
                holder.imageView.setImageResource(R.drawable.cod);
                break;
            case "cash":
                holder.imageView.setImageResource(R.drawable.paypal);
                break;
            case "freefire":
                holder.imageView.setImageResource(R.drawable.freefire1);
                break;
            case "lord":
                holder.imageView.setImageResource(R.drawable.lords);
                break;
            case "googleplay":
                holder.imageView.setImageResource(R.drawable.cardgg);
                break;
            case "netflix":
                holder.imageView.setImageResource(R.drawable.netflix);
                break;
            case "lol":
                holder.imageView.setImageResource(R.drawable.lol2);
                break;
            case "crossfire":
                holder.imageView.setImageResource(R.drawable.crossfire);
                break;
            case "fortnite":
                holder.imageView.setImageResource(R.drawable.fortnite);
                break;
            case "legand":
                holder.imageView.setImageResource(R.drawable.mobilelegends);
                break;
        }
        holder.rewardPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mInterstitialAd!=null){
                    mInterstitialAd.show((Activity) mContext);
                }
                if (interstitialAd.isReady()) {
                    interstitialAd.showAd();
                } else {
                    StartAppAd.showAd(mContext);
                }

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("typeOfRequest", model.getModelName());
                editor.putInt("cost", model.getCost());
                editor.putInt("userBudget", crowns);
                editor.putString("mainType", type);
                editor.apply();
                if (crowns >= model.getCost()) {
                    if (type.equals("cod")) {
                        codDialog dialog1 = new codDialog();
                        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        dialog1.show(manager, "codDialog");
                    }
                 if (type.equals("cash")) {
                        dialog dialog1 = new dialog();
                        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        dialog1.show(manager, "dialog");
                    } else if (type.equals("freefire")) {
                        freeFireDialog dialog1 = new freeFireDialog();
                        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        dialog1.show(manager, "freeFireDialog");
                    } else if (type.equals("lord")) {
                        lordsDialoge dialoge = new lordsDialoge();
                        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        dialoge.show(manager, "lordsDialoge");
                    }else if (type.equals("legand")) {
                            legand_dialoge dialoge = new legand_dialoge();
                            FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            dialoge.show(manager, "legand_dialoge");
                    }
//                 else if (type.equals("googleplay") || type.equals("netflix") || type.equals("lol") || type.equals("crossfire") || type.equals("fortnite")) {
//                        othersDialog dialog1 = new othersDialog();
//                        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
//                        dialog1.show(manager, "othersDialog");
//                    }
                } else {
                    insufficiencyDialog dialog2 = new insufficiencyDialog();
                    FragmentManager manager1 = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    dialog2.show(manager1, "insufficiencyDialog");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewardayaArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        private Button  rewardPrice;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rewardImg);
            name=itemView.findViewById(R.id.numberOfDollars);
            rewardPrice=itemView.findViewById(R.id.cashRequestButton);
        }
    }
    private void applovin() {
        AppLovinSdk.getInstance(mContext).setMediationProvider("max");
        AppLovinSdk.initializeSdk(mContext, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
            }
        });
        interstitialAd = new MaxInterstitialAd("2713e0f7b6a76771", (Activity) mContext);
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
        MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                com.google.android.gms.ads.interstitial.InterstitialAd.load(mContext, "ca-app-pub-6437456633370907/7720233800", adRequest, new InterstitialAdLoadCallback() {
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
                    mInterstitialAd=null;
                }
            });
        }
    }
}
