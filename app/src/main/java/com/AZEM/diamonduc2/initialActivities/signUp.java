package com.AZEM.diamonduc2.initialActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.ViewModel.UserViewModel;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class signUp extends AppCompatActivity {
    private EditText musername, mpassword, memailSignUp;
    private String username, password, friendId, phoneEmail, userId, imgUrl, email2;
    private CheckBox agree;
    private Button signUp;
    private String TAG = "sinUp";
    private boolean usernameCheck;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1221;
    private Integer inviterdiamonds;
    private static final String AUTH_TYPE = "rerequest";
    private static int j, k, l, m = 0;
    private RelativeLayout google;
    private CallbackManager mCallbackManager;
    private FirebaseFirestore db;
    private UserViewModel userViewModel;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private int inviteQuantity = 10;
    private Integer diamonds = 40;
    private Boolean isConnected = false;


    public static String random() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initiateWidgets();
        remoteConfig();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);
        LoginButton mLoginButton = findViewById(R.id.login_button);

        SharedPreferences preferences = getSharedPreferences("DYN", MODE_PRIVATE);
        friendId = preferences.getString("ref", "none");
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

        //Google Auth
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


        musername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    storeStrings();
                    if (TextUtils.isEmpty(username)) {
                        musername.setError("username is required");
                    } else if (username.length() < 8) {
                        musername.setError("username must be at least 6 characters");
                    }
                }
            }
        });

        memailSignUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    storeStrings();
                    if (TextUtils.isEmpty(phoneEmail)) {
                        //Toast.makeText(signUp.this, "First Name is required field", Toast.LENGTH_SHORT).show();
                        memailSignUp.setError("You have to enter email or phone number");
                        return;
                    }
                }
            }
        });

        mpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    storeStrings();
                    if (TextUtils.isEmpty(password)) {
                        mpassword.setError("password is required");
                        return;
                    } else if (password.length() < 8) {
                        mpassword.setError("password must be at least 8 characters");
                        return;
                    } else {
                    }
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(signUp.this, "Missing fields", Toast.LENGTH_SHORT).show();
                } else if (!agree.isChecked()) {
                    Toast.makeText(signUp.this, "You must agree our terms first", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Toast.makeText(signUp.this, "password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else if (usernameCheck) {
                    Toast.makeText(signUp.this, "use different username", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 6) {
                    Toast.makeText(signUp.this, "username must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    storeStrings();
                    dialog.setTitle("creating your account");
                    dialog.setMessage("please wait , we are checking your credentials");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    signUp(phoneEmail, username, password, friendId);
                }
            }
        });

    }

    private void invitingReward() {
        int i = 0;
        if (!friendId.equals("none") && i == 0) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(friendId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                int m = 0;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (m == 0) {
                        User user2 = dataSnapshot.getValue(User.class);
                        if (j == 0) {
                            assert user2 != null;
                            Integer oldC = user2.getInviteDiamond();
                            inviterdiamonds = oldC + inviteQuantity;
                            j++;
                        }
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("inviteDiamonds", inviterdiamonds);
                        databaseReference.updateChildren(hashMap);
                        String code = random();
                        HashMap<String, Object> map1 = new HashMap<>();
                        String transition = inviteQuantity + "diamonds earned from inviting a friend: " + code;
                        map1.put("details", transition);
                        db.collection(friendId).add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        });
                        m++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            i++;
        }
        i++;
    }

    private void signUp(final String email, final String username, final String password, final String referrerUid) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user4 = auth.getCurrentUser();
                    userId = user4.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("username", username);
                    map.put("diamonds", diamonds);
                    map.put("inviteDiamonds", 0);
                    reference.setValue(map);


                    ///todo
                    User user = new User(userId, diamonds, imgUrl, username);
                    userViewModel.insert(user);
//                    sharedPref(userId,username,diamonds,imgUrl);


                    Toast.makeText(signUp.this, "registered successfully", Toast.LENGTH_SHORT).show();
                    int i = 0;
                    if (!referrerUid.equals("none") && i == 0) {
                        invitingReward();
                        i++;
                    }
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
                    dialog.dismiss();
                    Toast.makeText(signUp.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initiateWidgets() {
        musername = findViewById(R.id.userNameSignUpEditText);
        mpassword = findViewById(R.id.password);
        agree = findViewById(R.id.agreeOurPolicies);
        signUp = findViewById(R.id.signUpBtn2);
        memailSignUp = findViewById(R.id.PhoneEmailSignUpEditText);
        dialog = new ProgressDialog(signUp.this);
        google = findViewById(R.id.googleSignUp);
        imgUrl = "https://firebasestorage.googleapis.com/v0/b/rewardedapp-c1b4c.appspot.com/o/man.png?alt=media&token=0e1ced12-040e-4d3e-8f8a-a527916a339c";

    }

    public void storeStrings() {
        username = musername.getText().toString();
        password = mpassword.getText().toString();
        phoneEmail = memailSignUp.getText().toString();
    }

    //Google Auth
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                if (account != null) {
                    invitingReward();
                    firebaseAuthWithGoogle(account, friendId);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        } else {
            invitingReward();
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account, String referrerUid) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "suc", Toast.LENGTH_LONG).show();
                Log.d(TAG, "firebaseAuthWithGoogle: sign in successfully");
                FirebaseUser user = auth.getCurrentUser();
                String userId2 = user.getUid();
                String username2 = user.getDisplayName();
                String imgURl = user.getPhotoUrl().toString();
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users");
                reference2.addValueEventListener(new ValueEventListener() {
                    int z = 0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (z == 0) {
                            if (!dataSnapshot.child(userId2).exists()) {

                                int i = 0;
                                if (!referrerUid.equals("none") && i == 0) {
                                    invitingReward();
                                    i++;
                                }

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("username", username2);
                                map.put("diamonds", diamonds);
                                map.put("inviteDiamonds", 0);
                                reference2.child(userId2).setValue(map);

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
                            z++;
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
                                int y = 0;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (y == 0) {
                                        if (!dataSnapshot.child(Uid).exists()) {

                                            int i = 0;
                                            if (!referrerUid.equals("none") && i == 0) {
                                                invitingReward();
                                                i++;
                                            }

                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("username", username);
                                            map.put("diamonds", diamonds);
                                            map.put("inviteDiamonds", 0);
                                            reference2.child(Uid).setValue(map);

                                            //todo
//                                        sharedPref(Uid,username,diamonds,imgUrl);
                                            User user = new User(Uid, diamonds, imgUrl, username);
                                            userViewModel.insert(user);

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
                                        y++;
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the User.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(signUp.this, "Authentication failed.",
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

    public void logInFromRegisterAc(View view) {
        startActivity(new Intent(this, login.class));
        finish();
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
                        .addOnCompleteListener(signUp.this, new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull Task<Boolean> task) {
                                if (task.isSuccessful()) {
                                    boolean updated = task.getResult();
                                    Log.d(TAG, "Config params updated: " + updated);
                                    inviteQuantity = (int) mFirebaseRemoteConfig.getLong("invite_quantity");
                                } else {
                                    Toast.makeText(signUp.this, "Fetch failed",
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