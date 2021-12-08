package com.example.easycheckin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.classes.Person;
import com.example.easycheckin.classes.Visits;
import com.example.easycheckin.databinding.ActivityMainBinding;
import com.example.easycheckin.databinding.EntryDoneBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AFI {

    private Bitmap temp;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> resultLauncher;
    private EntryDoneBinding entryDoneBinding;
    private AlertDialog entryDoneDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // Register activity result launcher for google sign in
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult() , result -> {
            if(result.getResultCode() != RESULT_OK) return;
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            task.addOnSuccessListener(googleSignInAccount -> {
                // Sign in success
                Helper.email = googleSignInAccount.getEmail();
                Helper.userName = googleSignInAccount.getDisplayName();

                getAccountDetails();
            });
            task.addOnFailureListener(e -> {
                // Sign in failed
                Toast.makeText(this , "Sign in failed" , Toast.LENGTH_SHORT).show();
            });
        });

        // Google Sign In Initialization
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        changeFragmentTo(new SplashFragment(), false);

        scanRes.observe(this , s -> {
            Visits visits = new Visits();
            visits.setDate(Helper.getDate());
            visits.setTime(Helper.getTime());
            visits.setUserId(Helper.email);
            visits.setInstitutionId(s);

            DataBaseHelper.markVisit(visits , new DataBaseHelper.Listener<Void>() {
                String name="";
                @Override
                public void onSuccess(Void unused){
                    showSuccessDialog(name);
                }

                @Override
                public void onFailure(String message){
                    Toast.makeText(MainActivity.this , message , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDataPassed(String data){
                    name = data;
                }
            });
        });

        buildEntryDoneDialog();
    }

    private void buildEntryDoneDialog( ){
        entryDoneBinding = EntryDoneBinding.inflate(getLayoutInflater());
        entryDoneDialog = new AlertDialog.Builder(this)
                .setView(entryDoneBinding.getRoot())
                .create();
        entryDoneBinding.gotIt.setOnClickListener(view -> entryDoneDialog.dismiss());
    }

    private void showSuccessDialog(String name){
        entryDoneBinding.name.setText(name);
        entryDoneDialog.show();
    }

    @Override
    protected void onStart( ){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            // do action
            Helper.email = account.getEmail();
            Helper.userName = account.getDisplayName();
            getAccountDetails();
        }else{
            new Handler().postDelayed(( ) -> changeFragmentTo(new AuthFragment(), true) , 1000);
        }
    }

    @Override
    public void getAccountDetails(){
        DataBaseHelper.getUserData(Helper.email , new DataBaseHelper.Listener<Person>() {
            @Override
            public void onSuccess(Person person){
                if(person != null){
                    Helper.myDetails = person;
                    changeFragmentTo(new UserDashBoardFragment(), true);
                }else{
                    DataBaseHelper.getInstituteData(Helper.email , new DataBaseHelper.Listener<Institute>() {
                        @Override
                        public void onSuccess(Institute institute){
                            if(institute == null){
                                // User not registered
                                changeFragmentTo(new AccountTypeFragment(), false);
                            }else{
                                Helper.myInstitute = institute;
                                changeFragmentTo(new InstitutionDashBoardFragment(), true);
                            }
                        }

                        @Override
                        public void onFailure(String message){
                            Toast.makeText(MainActivity.this , message , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String message){
                Toast.makeText(MainActivity.this , message , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            saveQRCode(temp);
        }else if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            changeFragmentTo(new ScanFragment(), false);
        }
    }

    public void savePhoto(Bitmap bitmap){
        if(bitmap == null) {
            Toast.makeText(this , "Something went wrong" , Toast.LENGTH_SHORT).show();
            return;
        }
        File dir = Environment.getExternalStorageDirectory();
        File ourFolder = new File(dir, "EasyCheckIn");
        if(!ourFolder.exists()){
            if(!ourFolder.mkdir()){
                Toast.makeText(this , "Unable to save" , Toast.LENGTH_SHORT).show();
            }
        }

        File file = new File(ourFolder, "QR Code.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this , "Saved" , Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeFragmentTo(Fragment fragment, boolean clearStack){
        if(clearStack) clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void clearBackStack( ){
        FragmentManager manager = getSupportFragmentManager();
        int n = manager.getBackStackEntryCount();
        for(int i=0;i<n;++i){
            manager.popBackStack();
        }
    }

    @Override
    public void saveQRCode(Bitmap bitmap){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            savePhoto(bitmap);
        }else{
            temp = bitmap;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void signIn( ){
        Intent intent = googleSignInClient.getSignInIntent();
        resultLauncher.launch(intent);
    }

    @Override
    public void signOut() {
        googleSignInClient.signOut()
                .addOnSuccessListener(unused -> {
                    // sign out success
                    changeFragmentTo(new AuthFragment(), true);
                })
                .addOnFailureListener(e -> {
                    // sign out error
                    Toast.makeText(this , "Sign out failed" , Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void startScanner( ){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            changeFragmentTo(new ScanFragment(), false);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        }
    }

    @Override
    public void popBackStack( ){
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed( ){
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
        }
    }
}