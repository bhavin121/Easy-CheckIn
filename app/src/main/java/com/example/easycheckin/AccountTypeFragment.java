package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easycheckin.classes.Person;
import com.example.easycheckin.databinding.FragmentAccountTypeBinding;
import com.example.easycheckin.databinding.FragmentAuthBinding;

public class AccountTypeFragment extends Fragment {

    private AFI afi;

    public AccountTypeFragment( ){
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
        return inflater.inflate(R.layout.fragment_account_type , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        FragmentAccountTypeBinding binding = FragmentAccountTypeBinding.bind(view);

        binding.institution.setOnClickListener(view1 -> {
            afi.changeFragmentTo(new InstitutionRegisterFragment(), true);
        });

        binding.personal.setOnClickListener(view1 -> {
            afi.changeFragmentTo(new PersonalRegisterFragment(), true);
        });

    }
}