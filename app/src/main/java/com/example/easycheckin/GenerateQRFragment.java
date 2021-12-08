package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easycheckin.databinding.FragmentGenerateQRBinding;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateQRFragment extends Fragment {


    private AFI afi;

    public GenerateQRFragment( ){
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
        return inflater.inflate(R.layout.fragment_generate_q_r , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        FragmentGenerateQRBinding binding = FragmentGenerateQRBinding.bind(view);

        String text = Helper.myInstitute.getEmail();
        QRGEncoder encoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, convertDpToPx(300));
        binding.qrcode.setImageBitmap(encoder.getBitmap());

        binding.qrview.setDrawingCacheEnabled(true);
        binding.save.setOnClickListener(view1 -> afi.saveQRCode(binding.qrview.getDrawingCache()));


    }

    public int convertDpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}