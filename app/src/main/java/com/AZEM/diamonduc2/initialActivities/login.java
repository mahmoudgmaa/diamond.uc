package com.AZEM.diamonduc2.initialActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.dialogs.forgetDialoge;
import com.AZEM.diamonduc2.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class login extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private String email, password, imgUrl;
    private static final int RC_SIGN_IN = 1221;
    private ProgressDialog dialog;
    private FirebaseAuth firebaseAuth;
    private static final String EMAIL = "email";
    private TextView forget;
    private static final String USER_POSTS = "user_posts";
    private String userId;
    private static final String AUTH_TYPE = "rerequest";
    private Button logInBtn3, signUpBtn3;
    private RelativeLayout google;
    private Integer diamonds = 40;
    private String TAG = "logIn";
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager mCallbackManager;
    private String friendId;
    private Integer inviterdiamonds;
    private FirebaseFirestore db;
    private UserViewModel userViewModel;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private int inviteQuantity = 10;
    private static int j = 0;
    private Boolean isConnected = false;


    public static String random() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialWidgets();
        remoteConfig();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

        SharedPreferences preferences = getSharedPreferences("DYN", MODE_PRIVATE);
        friendId = preferences.getString("ref", "none");


        //facebook auth
        LoginButton mLoginButton = findViewById(R.id.login_button2);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setPermissions(Arrays.asList(EMAIL));
        mLoginButton.setAuthType(AUTH_TYPE);
        // Register a callback to respond to the User
        mLoginButton.registerCallback(
                mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        dialog.setTitle("creating your account");
                        dialog.setMessage("please wait , we are checking your credentials");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        handleFacebookAccessToken(loginResult.getAccessToken(), friendId);
                    }

                    @Override
                    public void onCancel() {
                        setResult(RESULT_CANCELED);
                    }

                    @Override
                    public void onError(FacebookException e) {
                        // Handle exception
                    }
                });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("creating your account");
                dialog.setMessage("please wait , we are checking your credentials");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetDialoge dialog2 = new forgetDialoge();
                FragmentManager manager1 = getSupportFragmentManager();
                dialog2.show(manager1, "forgetDialoge");
            }
        });

        signUpBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signUp.class));
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    storeStrings();
                    if (TextUtils.isEmpty(email)) {
                        mEmail.setBackgroundResource(R.drawable.button4);
                        mEmail.setError("you have to enter Email address or phone number");
                        return;
                    }else {
                        mEmail.setBackgroundResource(R.drawable.button);
                    }
                }

            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    storeStrings();
                    if(TextUtils.isEmpty(password)){
                        mPassword.setBackgroundResource(R.drawable.button4);
                        mPassword.setError("password is required");
                        return;
                    }else {
                        mPassword.setBackgroundResource(R.drawable.button);
                    }
                }
            }
        });

        logInBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeStrings();
                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(login.this, "Missing fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.setTitle("logging you in");
                    dialog.setMessage("please wait , we are checking your credentials");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                userId = user.getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("inviteDiamonds", 0);
                                        reference.setValue(map);
                                        User user1 = snapshot.getValue(User.class);
                                        assert user1 != null;
                                        //todo
//                                        sharedPref(userId,user1.getUsername(),user1.getdiamonds(),imgUrl);
                                        User user = new User(userId, user1.getDiamonds(), imgUrl, user1.getUsername());
                                        userViewModel.insert(user);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                Toast.makeText(login.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), firstScreen.class));
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();
                            }else{
                                dialog.dismiss();
                                Toast.makeText(login.this, "Error! "+ task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                forget.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void invitingReward() {
        int i = 0;
        if (!friendId.equals("none") && i == 0) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(friendId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                int z = 0;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (z == 0) {
                        User user2 = dataSnapshot.getValue(User.class);
                        if (j == 0) {
                            assert user2 != null;
                            int oldC = user2.getInviteDiamond();
                            inviterdiamonds = oldC + inviteQuantity;
                            j++;
                        }
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("inviteDiamonds", inviterdiamonds);
                        databaseReference.updateChildren(hashMap);
                        String code = random();
                        HashMap<String, Object> map1 = new HashMap<>();
                        String transition = "50 diamonds earned from inviting a friend: " + code;
                        map1.put("details", transition);
                        db.collection(friendId).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        });
                        z++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        i++;
    }


    private void initialWidgets() {
        mEmail = findViewById(R.id.PhoneEmailLogInEditText);
        mPassword = findViewById(R.id.passwordLoginEditText);
        logInBtn3 = findViewById(R.id.logInBtn3);
        dialog = new ProgressDialog(login.this);
        firebaseAuth = FirebaseAuth.getInstance();
        forget = findViewById(R.id.forget);
        signUpBtn3 = findViewById(R.id.signUpBtn3);
        google = findViewById(R.id.google1);
        imgUrl = "https://firebasestorage.googleapis.com/v0/b/rewardedapp-c1b4c.appspot.com/o/man.png?alt=media&token=0e1ced12-040e-4d3e-8f8a-a527916a339c";
    }

    private void storeStrings() {
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                if (account != null) {
                    firebaseAuthWithGoogle(account, friendId);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account, String referrerUid) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "firebaseAuthWithGoogle: sign in successfully");
                FirebaseUser user = auth.getCurrentUser();
                String userId2 = user.getUid();
                String username2 = user.getDisplayName();
                String imgURl = user.getPhotoUrl().toString();
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users");
                reference2.addValueEventListener(new ValueEventListener() {
                    int y = 0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (y == 0) {
                            if (!dataSnapshot.child(userId2).exists()) {
                                int i = 0;
                                if (!referrerUid.equals("none") && i == 0) {
                                    invitingReward();
                                    i++;
                                }
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId2);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("username", username2);
                                map.put("diamonds", diamonds);
                                map.put("inviteDiamonds", 0);
                                reference.setValue(map);
                                //todo
                                User user = new User(userId2, diamonds, imgURl, username2);
                                userViewModel.insert(user);
//                            sharedPref(userId2,username2,diamonds,imgURl);
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), firstScreen.class));
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();

                            } else {
                                User user2 = new User(userId2, 400, imgURl, username2);
                                userViewModel.insert(user2);

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), firstScreen.class));
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();

                            }
                            y++;
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token, String referrerUid) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in User's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            String Uid = user.getUid();
                            String username = user.getDisplayName();
                            String imgUrl = user.getPhotoUrl().toString();
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users");
                            reference2.addValueEventListener(new ValueEventListener() {
                                int x = 0;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (x == 0) {
                                        if (!dataSnapshot.child(Uid).exists()) {
                                            int i = 0;
                                            if (!referrerUid.equals("none") && i == 0) {
                                                invitingReward();
                                                i++;
                                            }

                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("username", username);
                                            map.put("diamonds", diamonds);
                                            map.put("inviteDiamonds", 0);
                                            reference.setValue(map);

                                            //todo
                                            User user = new User(Uid, diamonds, imgUrl, username);
                                            userViewModel.insert(user);
//                                        sharedPref(Uid,username,diamonds,imgUrl);


                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();
                                                    finish();
                                                    startActivity(new Intent(getApplicationContext(), firstScreen.class));
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            thread.start();

                                        } else {
                                            User user2 = new User(Uid, 400, imgUrl, username);
                                            userViewModel.insert(user2);
                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();
                                                    finish();
                                                    startActivity(new Intent(getApplicationContext(), firstScreen.class));
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            thread.start();


                                        }
                                        x++;
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the User.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
                        .addOnCompleteListener(login.this, new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull Task<Boolean> task) {
                                if (task.isSuccessful()) {
                                    boolean updated = task.getResult();
                                    Log.d(TAG, "Config params updated: " + updated);
                                    inviteQuantity = (int) mFirebaseRemoteConfig.getLong("invite_quantity");
                                } else {
                                    Toast.makeText(login.this, "Fetch failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        thread.run();
    }

    public void sharedPref(String id, String username, int diamonds, String imgUrl) {
        SharedPreferences preferences = getSharedPreferences("userData", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", id);
        editor.putString("username", username);
        editor.putString("imgUrl", imgUrl);
        editor.putInt("diamonds", diamonds);
        editor.apply();
    }

}