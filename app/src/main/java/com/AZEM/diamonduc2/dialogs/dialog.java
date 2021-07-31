package com.AZEM.diamonduc2.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.models.User;
import com.AZEM.diamonduc2.navigatorFragments.mainFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class dialog extends DialogFragment {
    private Button done;
    private Button cancel;
    private MaterialEditText phone;
    private String typeOfRequest;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private Integer crowns = 0;
    private Integer cost = 0;
    private static final int MAX_LENGTH = 9;
    private FirebaseFirestore db;
    private String TAG = "dialoge";
    private SharedPreferences preferences;
    private String today;


    private UserViewModel userViewModel;


    public static String random() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    //    private InterstitialAd mInterstitialAd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_cash_dialog, container, false);
        db = FirebaseFirestore.getInstance();
        preferences = getActivity().getSharedPreferences("requests", 0);

        done = view.findViewById(R.id.cashDoneRequest);
        cancel = view.findViewById(R.id.cashCancelRequest);
        phone = view.findViewById(R.id.phoneNumberEditText);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        userViewModel = new ViewModelProvider(getActivity(), ViewModelProvider
                .AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        typeOfRequest = prefs.getString("typeOfRequest", "none");
        cost = prefs.getInt("cost", 0);
        crowns = prefs.getInt("userBudget", 0);
        String type = "cash";
        String requestCondition = "pending";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        today = year + "" + month + "" + day;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() == 0) {
                    phone.setError("Enter your paypal account");
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("requests")
                            .child(currentUser.getUid()).child(typeOfRequest + "nv20");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("requestName", typeOfRequest);
                    map.put("paypalaccount", phone.getText().toString());
                    map.put("requestType", type);
                    map.put("requestCondition", requestCondition);
                    reference.setValue(map);

                    Integer newCrowns = 0;
                    newCrowns = crowns - cost;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(today + "requests", false);
                    editor.apply();

                    userViewModel.getUser(currentUser.getUid()).observe(getActivity(), new Observer<User>() {
                        int i = 0;

                        @Override
                        public void onChanged(User user) {
                            if (i == 0) {
                                int crowns = user.getDiamonds();
                                crowns = crowns - cost;
                                user.setDiamonds(crowns);
                                userViewModel.update(user);
                                i++;
                            }

                        }
                    });

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("crowns", newCrowns);
                    reference1.updateChildren(map1);
                    mainFragment rewards1 = (mainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mainFragment");

                    String code = random();
                    HashMap<String, Object> map2 = new HashMap<>();
                    String transition = cost + " diamonds spent on a request with code: " + code;
                    map2.put("details", transition);
//                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("history").child(currentUser.getUid()).child(code);
//                    reference2.setValue(map2);
                    db.collection(currentUser.getUid()).add(map2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    });

                    getDialog().dismiss();
                }
            }
        });
        return view;
    }
}
