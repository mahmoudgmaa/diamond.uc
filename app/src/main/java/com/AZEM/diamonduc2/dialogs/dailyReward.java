package com.AZEM.diamonduc2.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.initialActivities.MainActivity;
import com.AZEM.diamonduc2.models.User;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.Calendar;
import java.util.HashMap;

public class dailyReward extends DialogFragment {
    int count;
    private Button day1, day2, day3, day4, day5, day6, day7;
    private boolean currentDay;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String TAG = "daily";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_reward, container, false);
        day1 = view.findViewById(R.id.day1);
        day2 = view.findViewById(R.id.day2);
        day3 = view.findViewById(R.id.day3);
        day4 = view.findViewById(R.id.day4);
        day5 = view.findViewById(R.id.day5);
        day6 = view.findViewById(R.id.day6);
        day7 = view.findViewById(R.id.day7);
        db = FirebaseFirestore.getInstance();


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today = year + "" + month + "" + day;
        SharedPreferences preferences = getActivity().getSharedPreferences("daily", 0);
        count = preferences.getInt("count", 1);
        int lastDay = preferences.getInt("day", day);

        if (day - lastDay == 1) {
            if (count == 1) {
                day1.setVisibility(View.VISIBLE);
            } else if (count == 2) {
                day2.setVisibility(View.VISIBLE);
            } else if (count == 3) {
                day3.setVisibility(View.VISIBLE);
            } else if (count == 4) {
                day4.setVisibility(View.VISIBLE);
            } else if (count == 5) {
                day5.setVisibility(View.VISIBLE);
            } else if (count == 6) {
                day6.setVisibility(View.VISIBLE);
            } else if (count == 7) {
                day7.setVisibility(View.VISIBLE);
            }
        } else {
            count = 1;
            day1.setVisibility(View.VISIBLE);
        }
        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 5;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = count + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();

                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 5 + " diamonds earned from daily reward in day1: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });


                getDialog().dismiss();

            }
        });
        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 10;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = count + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();
                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 10 + " diamonds earned from daily reward in day2: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });
                getDialog().dismiss();
            }
        });
        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 30;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = count + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();

                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 30 + " diamonds earned from daily reward in day3: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

                getDialog().dismiss();
            }
        });
        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 50;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = count + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();

                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 50 + " diamonds earned from daily reward in day4: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

                getDialog().dismiss();
            }
        });
        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 90;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = count + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();

                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 90 + " diamonds earned from daily reward in day5: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

                getDialog().dismiss();
            }
        });
        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 120;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = count + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();

                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 120 + " diamonds earned from daily reward in day6: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

                getDialog().dismiss();
            }
        });
        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUser(user.getUid()).observe(getActivity(), new Observer<User>() {
                    int i = 0;

                    @Override
                    public void onChanged(User user) {
                        if (i == 0) {
                            int diamonds = user.getDiamonds();
                            int newdiamonds = diamonds + 200;
                            user.setDiamonds(newdiamonds);
                            userViewModel.update(user);
                            i++;
                        }
                    }
                });
                int newCount = 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(today, true);
                editor.putInt("day", day);
                editor.putInt("count", newCount);
                editor.apply();

                HashMap<String, Object> map1 = new HashMap<>();
                String transition = 200 + " diamonds earned from daily reward in day7: " + today;
                map1.put("details", transition);
                db.collection(user.getUid()).document("dailyReward").collection("history").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Dialog yourDialog = getDialog();
        yourDialog.getWindow().setLayout((7 * width) / 7, (2 * height) / 5);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
