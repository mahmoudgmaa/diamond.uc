package com.AZEM.diamonduc2.navigatorFragments.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.AZEM.diamonduc2.ViewModel.UserViewModel;
import com.AZEM.diamonduc2.initialActivities.firstScreen;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.bumptech.glide.Glide;
import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.models.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.startapp.sdk.adsbase.AutoInterstitialPreferences;
import com.startapp.sdk.adsbase.StartAppAd;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class editProfile extends AppCompatActivity {
    private ImageView editProfileImage, profileImage, close;
    private MaterialEditText usernameEditText;
    private Button save;
    private Uri imgURi;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private String TAG = "editProfile";
    private Button logOut;
    private AlertDialog.Builder dialog;
    private FirebaseAuth auth;
    private UserViewModel userViewModel;
    private MaxAdView adView0;

    private AdView AdV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initiateWidgets();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
//        AdV=findViewById(R.id.adView152);
//        AdRequest request=new AdRequest.Builder().build();
//        AdV.loadAd(request);

        StartAppAd.showAd(this);

        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
                adView0.loadAd();
                adView0.setVisibility( View.VISIBLE );
                adView0.startAutoRefresh();
            }
        } );

        auth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = auth.getCurrentUser();
        userViewModel.getUser(firebaseUser.getUid()).observe(this, new Observer<User>() {

            @Override
            public void onChanged(User user) {
                usernameEditText.setText(user.getUsername());
                Glide.with(getApplicationContext())
                        .load(user.getImgUrl())
                        .into(profileImage);

            }
        });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(editProfile.this);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(usernameEditText.getText().toString());
                finish();
            }
        });

        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(editProfile.this);
                dialog.setTitle("Log out");
                dialog.setMessage("Are you sure to log out from your account");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        userViewModel.deleteAllUser();
                        startActivity(new Intent(editProfile.this, firstScreen.class));
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.create();
                dialog.show();
            }
        });
    }

    private void updateProfile(String username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", username);
        reference.updateChildren(hashMap);
        userViewModel.getUser(firebaseUser.getUid()).observe(editProfile.this, new Observer<User>() {
            int i = 0;

            @Override
            public void onChanged(User user) {
                if (i == 0) {
                    user.setUsername(username);
                    userViewModel.update(user);
                    i++;
                }
            }
        });
    }

    public void initiateWidgets() {
        editProfileImage = findViewById(R.id.editProfileImage);
        profileImage = findViewById(R.id.profileImage);
        close = findViewById(R.id.backFromEditDetails);
        usernameEditText = findViewById(R.id.usernameEditText);
        save = findViewById(R.id.saveEditing);
        adView0=findViewById(R.id.adView11);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Updating");
        dialog.show();
        if (imgURi != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "" + getFileExtension(imgURi));
            uploadTask = reference.putFile(imgURi);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String Uri = downloadUri.toString();

                        userViewModel.getUser(firebaseUser.getUid()).observe(editProfile.this, new Observer<User>() {
                            @Override
                            public void onChanged(User user) {
                                user.setImgUrl(Uri);
                                userViewModel.update(user);
                            }
                        });

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imgUrl", Uri);
                        reference1.updateChildren(hashMap);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(editProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(editProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(editProfile.this, "no image selected", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgURi = result.getUri();
            uploadImage();
        } else {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}