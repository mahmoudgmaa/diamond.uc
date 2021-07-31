package com.AZEM.diamonduc2.initialActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class firstScreen extends AppCompatActivity {
    private Button signUp, logIn;
    private FirebaseAuth firebaseAuth;
    private Integer crowns;
    private String name;
    private String TAG = "firstScreen";
    private UserViewModel userViewModel;


    public void initiateWidgets() {
        signUp = findViewById(R.id.signUpBtn);
        logIn = findViewById(R.id.logInBtn);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void clickListeners() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(firstScreen.this, signUp.class));
                finish();
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(firstScreen.this, login.class));
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        initiateWidgets();
        clickListeners();


        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(firstScreen.this, MainActivity.class));
            finish();
        }

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the User isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the User anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        if (user1 == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            // Toast.makeText(getApplicationContext(),""+referrerUid, Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = getSharedPreferences("DYN", MODE_PRIVATE).edit();
                            editor.putString("ref", referrerUid);
                            editor.apply();

                        }
                    }
                });


    }
}
