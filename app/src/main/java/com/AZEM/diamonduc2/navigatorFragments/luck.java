package com.AZEM.diamonduc2.navigatorFragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.dialogs.insufficiencyDialog;
import com.AZEM.diamonduc2.models.User;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

public class luck extends Fragment {
    private ImageView gift;
    private TextView date1;
    private Button generateLucky;
    private TextView winnerName, winnerCode, usersCode;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private ProgressDialog dialog;
    private String username;
    private Boolean isExist = false;
    private UserViewModel userViewModel;
    private MaxInterstitialAd interstitialAd;
    private int hour_now;
    private boolean currentDay;
    private String code;
    private SharedPreferences preferences;
    private String today;
    private static final int MAX_LENGTH = 7;
    int daygift;
    private FirebaseFirestore db;

    private InterstitialAd mInterstitialAd;

    private static int i=0;

    @Override
    public void onStart() {
        super.onStart();
        adMob();
        applovin();
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < MAX_LENGTH; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_luck, container, false);
        initialWidgets(view);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        applovin();
        existance();
        adMob();


//        if(interstitialAd.isReady()){
//            interstitialAd.showAd();
//        }


        StartAppAd.showAd(getActivity());

        TimeZone zone = TimeZone.getTimeZone("Asia/Riyadh");
        Calendar calendar = new GregorianCalendar(zone);
        hour_now = calendar.get(Calendar.HOUR_OF_DAY);
        daygift = (int) calendar.get(Calendar.DAY_OF_MONTH);
        int hours_to_open = 14 - hour_now;
        boolean currentDayImg = preferences.getBoolean(today + "image", false);
        if (hours_to_open < 0) {
            hours_to_open = hours_to_open + 24;
        }
        if (hour_now >= 14 && hour_now <= 16) {
            generateLucky.setVisibility(View.VISIBLE);
            date1.setText("The Reward");
            if (!currentDayImg) {
                imageSelection(daygift);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today + "image", true);
                editor.apply();
            }
            imageSelection(daygift);
        } else {
            generateLucky.setVisibility(View.GONE);
            date1.setText("Will be available in " + hours_to_open + " hours");
            usersCode.setText("N/A");
        }
        initialingWinner();
        writingWinner();

        generateLucky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("generating your lucky code");
                dialog.setMessage("please wait , we are generating your lucky code");
                dialog.setCanceledOnTouchOutside(false);
                if (!isExist) {
                    userViewModel.getUser(currentUser.getUid()).observe(getActivity(), new Observer<User>() {
                        int i = 0;

                        @Override
                        public void onChanged(User user) {
                            int crowns = user.getDiamonds();
                            if (i == 0) {
                                if (crowns >= 30) {
                                    dialog.show();
                                    generateCode();
                                } else {
                                    if(mInterstitialAd!=null){
                                        mInterstitialAd.show(getActivity());
                                    }else if (interstitialAd.isReady()) {
                                        interstitialAd.showAd();
                                    } else {
                                        StartAppAd.showAd(getActivity());
                                    }
                                    Toast.makeText(getActivity(), "you don't have enough crowns", Toast.LENGTH_SHORT).show();
                                    insufficiencyDialog dialog2 = new insufficiencyDialog();
                                    dialog2.show(getChildFragmentManager(), "insufficiencyDialog");
                                }
                                i++;
                            }
                        }
                    });
                } else {
                     if (mInterstitialAd!=null) {
                         mInterstitialAd.show(getActivity());
                    }else if(interstitialAd.isReady()){
                        interstitialAd.showAd();
                    } else {
                        StartAppAd.showAd(getActivity());
                    }
                    Toast.makeText(getActivity(), "you already generated your lucky code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private void generateCode() {
        userViewModel.getUser(currentUser.getUid()).observe(getActivity(), new Observer<User>() {
            int i = 0;

            @Override
            public void onChanged(User user) {
                if (i == 0) {
                    int crowns = user.getDiamonds();
                    int newC = crowns - 30;
                    user.setDiamonds(newC);
                    username = user.getUsername();
                    userViewModel.update(user);
                    HashMap<String, Object> map2 = new HashMap<>();
                    map2.put("crowns", newC);
                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                    reference3.updateChildren(map2);
                    code = random();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("code", code);
                    editor.putBoolean(today + "code", true);
                    editor.apply();
                    existance();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("diamondLuckyTow").child(currentUser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("code", code);
                    map.put("username", username);
                    reference1.setValue(map);
                    i++;
                }

            }
        });
        dialog.dismiss();

         if (mInterstitialAd!=null) {
             mInterstitialAd.show(getActivity());
        }else if(interstitialAd.isReady()){
            interstitialAd.showAd();
        }else {
            StartAppAd.showAd(getActivity());
        }
    }

    private void initialWidgets(View view) {
        dialog = new ProgressDialog(getActivity());
        date1 = view.findViewById(R.id.date);
        gift = view.findViewById(R.id.gift);
        generateLucky = view.findViewById(R.id.generateLucky);
        winnerName = view.findViewById(R.id.winnerName);
        winnerCode = view.findViewById(R.id.winnerCode);
        usersCode = view.findViewById(R.id.usersCode);
        userViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);
    }

    private void existance() {
        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        today = year + "" + month + "" + day;
        preferences = getActivity().getSharedPreferences("diamondCode", 0);
        currentDay = preferences.getBoolean(today + "code", false);
        code = preferences.getString("code", "N/A");
        if (currentDay) {
            isExist = true;
            usersCode.setText(code);
        } else {
            usersCode.setText("N/A");
            isExist = false;
        }
    }

    private void initialingWinner() {
        if (hour_now < 16) {
            boolean winnerBefore19 = preferences.getBoolean(today + "winnerBefore19", false);
            if (!winnerBefore19) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("diamondWinner");
                reference.addValueEventListener(new ValueEventListener() {
                    int y = 0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (y == 0) {
                            String winnerName = dataSnapshot.child("username").getValue().toString();
                            String winnerCode = dataSnapshot.child("code").getValue().toString();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(today + "winnerBefore19", true);
                            editor.putString("winnerName", winnerName);
                            editor.putString("winnerCode", winnerCode);
                            editor.apply();
                            y++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } else if (hour_now >= 16) {
            boolean winnerAfter19 = preferences.getBoolean(today + "winnerAfter19", false);
            if (!winnerAfter19) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("diamondWinner");
                reference.addValueEventListener(new ValueEventListener() {
                    int x = 0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (x == 0) {
                            String winnerName = dataSnapshot.child("username").getValue().toString();
                            String winnerCode = dataSnapshot.child("code").getValue().toString();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(today + "winnerAfter19", true);
                            editor.putString("winnerName", winnerName);
                            editor.putString("winnerCode", winnerCode);
                            editor.apply();
                            x++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

    private void applovin() {
        interstitialAd = new MaxInterstitialAd("0ffb50fc365c6085", getActivity());
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

    private void imageSelection(int dayC) {
        switch (dayC) {
            case 1/*"df110"*/:
                gift.setImageResource(R.drawable.df110);
                break;
            case 26/*"df110"*/:
                gift.setImageResource(R.drawable.df110);
                break;
            case 6/*"df231"*/:
                gift.setImageResource(R.drawable.df231);
                break;
            case 11/*"df530"*/:
                gift.setImageResource(R.drawable.df530);
                break;
            case 16/*"df1188"*/:
                gift.setImageResource(R.drawable.df1188);
                break;
            case 20/*"df2420"*/:
                gift.setImageResource(R.drawable.df2420);
                break;
            case 2/*"uc60"*/:
                gift.setImageResource(R.drawable.uc60);
                break;
            case 28/*"uc60"*/:
                gift.setImageResource(R.drawable.uc60);
                break;
            case 7/*"uc190"*/:
                gift.setImageResource(R.drawable.uc190);
                break;
            case 12/*"uc325"*/:
                gift.setImageResource(R.drawable.uc325);
                break;
            case 17/*"uc660"*/:
                gift.setImageResource(R.drawable.uc660);
                break;
            case 21/*"uc1800"*/:
                gift.setImageResource(R.drawable.uc1800);
                break;
            case 24/*"ucrp"*/:
                gift.setImageResource(R.drawable.ucrp);
                break;
            case 3/*"ml16"*/:
                gift.setImageResource(R.drawable.ml16);
                break;
            case 27/*"ml16"*/:
                gift.setImageResource(R.drawable.ml16);
                break;
            case 8/*"ml44"*/:
                gift.setImageResource(R.drawable.ml44);
                break;
            case 13/*"ml89"*/:
                gift.setImageResource(R.drawable.ml89);
                break;
            case 18/*"ml133"*/:
                gift.setImageResource(R.drawable.ml133);
                break;
            case 23/*"ml221"*/:
                gift.setImageResource(R.drawable.ml221);
                break;
            case 4/*"cr1000"*/:
                gift.setImageResource(R.drawable.cr1000);
                break;
            case 29/*"cr1000"*/:
                gift.setImageResource(R.drawable.cr1000);
                break;
            case 22/*"cr1000"*/:
                gift.setImageResource(R.drawable.cr1000);
                break;
            case 9/*"cr1200"*/:
                gift.setImageResource(R.drawable.cr1200);
                break;
            case 14/*"cr1500"*/:
                gift.setImageResource(R.drawable.cr1500);
                break;
            case 19/*"cr2000"*/:
                gift.setImageResource(R.drawable.cr2000);
                break;
            case 5/*"cash1"*/:
                gift.setImageResource(R.drawable.cash1);
                break;
            case 30/*"cash1"*/:
                gift.setImageResource(R.drawable.cash1);
                break;
            case 25/*"cash1"*/:
                gift.setImageResource(R.drawable.cash1);
                break;
            case 10/*"cash5"*/:
                gift.setImageResource(R.drawable.cash5);
                break;
            case 15/*"cash10"*/:
                gift.setImageResource(R.drawable.cash10);
                break;
            default:
                gift.setImageResource(R.drawable.df110);
        }
    }

    private void writingWinner() {
        initialingWinner();
        String winnerNameStr = preferences.getString("winnerName", "N/A");
        String winnerCodeStr = preferences.getString("winnerCode", "N/A");
        winnerName.setText(winnerNameStr);
        winnerCode.setText(winnerCodeStr);
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
                        if(i==0) {
                            mInterstitialAd.show(getActivity());
                            i++;
                        }
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