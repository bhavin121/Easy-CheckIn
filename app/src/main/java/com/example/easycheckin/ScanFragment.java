package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.easycheckin.databinding.FragmentScanBinding;

public class ScanFragment extends Fragment {

    private AFI afi;
    private CodeScanner mCodeScanner;

    public ScanFragment( ){
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof AFI){
            afi = (AFI) context;
        }else{
            throw new RuntimeException(context.getClass() +" must implement AFI");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        FragmentScanBinding binding = FragmentScanBinding.bind(view);

        mCodeScanner = new CodeScanner(requireContext(), binding.scannerView);
        mCodeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(( ) -> {
            afi.scanRes.postValue(result.getText());
            afi.popBackStack();
        }));
        binding.scannerView.setOnClickListener(view1 -> mCodeScanner.startPreview());
    }

    @Override
    public void onResume( ) {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause( ) {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}