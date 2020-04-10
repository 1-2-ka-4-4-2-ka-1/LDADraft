package com.bankbros.ldaadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginActvity extends AppCompatActivity {



    private int STORAGE_PERMISSION_CODE = 1;



    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String adminId="";


    int AUTH_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        if (ContextCompat.checkSelfPermission(LoginActvity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permition Granted Already", Toast.LENGTH_SHORT).show();
            doLogin();
        } else {
            requestStoragePermition();
        }
        SplashScreenActivity.splash_activity.finish();

    }



    public void doLogin(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
//                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_launcher_foreground)
                .setAlwaysShowSignInMethodScreen(true)
                .setTheme(R.style.AppTheme)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent,AUTH_CODE);
    }

    private void requestStoragePermition() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permition is needed to proceed...")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(LoginActvity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            LoginActvity.this.finish();
                            System.exit(0);
                        }
                    }).create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                doLogin();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AUTH_CODE && resultCode == RESULT_OK){

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null) {


                databaseReference.child("Admin").child("id").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                        adminId = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        Log.i("id", "onChildAdded: "+dataSnapshot.getValue().toString()+user.getEmail());

                        if(!adminId.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            Toast.makeText(getBaseContext(), "You are not authorized as admin", Toast.LENGTH_SHORT).show();
                            Log.i("id", "onChildAdded: "+"logout");
                            user.delete();

                            ((ActivityManager) Objects.requireNonNull(LoginActvity.this.getSystemService(ACTIVITY_SERVICE)))
                                    .clearApplicationUserData();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    LoginActvity.this.finish();
                                }
                            },5000);


                        }else {
                            Intent intent = new Intent(LoginActvity.this,DashBoardActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }
        SplashScreenActivity.splash_activity.finish();
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private boolean visible = false;

    @Override
    protected void onStart() {
        super.onStart();

        if(!visible){

            visible = true;
        }
    }
}
