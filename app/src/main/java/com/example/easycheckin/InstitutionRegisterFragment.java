package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.databinding.FragmentInstitutionRegisterBinding;
import com.example.easycheckin.databinding.FragmentPersonalRegisterBinding;

public class InstitutionRegisterFragment extends Fragment {

    private AFI afi;

    public InstitutionRegisterFragment( ){
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
        return inflater.inflate(R.layout.fragment_institution_register , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);

        FragmentInstitutionRegisterBinding binding = FragmentInstitutionRegisterBinding.bind(view);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new String[]{"Educational", "Bank", "Government", "Corporate Office", "Public Place"});
        binding.type.setAdapter(genderAdapter);

        binding.submit.setOnClickListener(view1 -> {
            Institute institute = new Institute();
            institute.setEmail(Helper.email);
            institute.setInstitutionType(binding.type.getText().toString());
            institute.setAddress(binding.address.getText().toString());
            institute.setMobileNo(binding.mobile.getText().toString());
            institute.setName(binding.name.getText().toString());
            institute.setCity(binding.city.getText().toString());
            institute.setState(binding.state.getText().toString());
            institute.setCountry(binding.country.getText().toString());

            DataBaseHelper.registerInstitution(institute , new DataBaseHelper.Listener<Void>() {
                @Override
                public void onSuccess(Void unused){
                    Helper.myInstitute = institute;
                    afi.changeFragmentTo(new InstitutionDashBoardFragment(), true);
                    Toast.makeText(requireContext() , "Success" , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String message){
                    Toast.makeText(requireContext() , message , Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}