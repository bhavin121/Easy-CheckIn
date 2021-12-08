package com.example.easycheckin;

import android.graphics.Bitmap;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.easycheckin.classes.Person;

/**
 * AFI is Activity Fragment Interface
 * Helps fragment to access activity methods
 */

public interface AFI {
    MutableLiveData<String> email = new MutableLiveData<>();
    MutableLiveData<String> scanRes = new MutableLiveData<>();
    void changeFragmentTo(Fragment fragment, boolean clearStack);
    void saveQRCode(Bitmap bitmap);
    void signIn();
    void signOut();
    void startScanner();
    void popBackStack();
    void getAccountDetails();
}
