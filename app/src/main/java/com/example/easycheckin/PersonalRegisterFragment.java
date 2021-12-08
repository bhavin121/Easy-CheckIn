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

import com.example.easycheckin.classes.Person;
import com.example.easycheckin.databinding.FragmentPersonalRegisterBinding;
import com.google.android.material.textfield.TextInputLayout;

public class PersonalRegisterFragment extends Fragment {

    private AFI afi;

    public PersonalRegisterFragment( ){
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
        return inflater.inflate(R.layout.fragment_personal_register , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        FragmentPersonalRegisterBinding binding = FragmentPersonalRegisterBinding.bind(view);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new String[]{"Male", "Female"});
        binding.gender.setAdapter(genderAdapter);

        ArrayAdapter<String> covidAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new String[]{"Positive Now", "Positive in Past", "Negative"});
        binding.covid.setAdapter(covidAdapter);

        binding.name.setText(Helper.userName);

        binding.submit.setOnClickListener(view1 -> {
            Person person = new Person();
            person.setEmail(Helper.email);
            person.setAge(Integer.parseInt(binding.age.getText().toString()));
            person.setName(binding.name.getText().toString());
            person.setMobileNo(binding.mobile.getText().toString());
            person.setCity(binding.city.getText().toString());
            person.setState(binding.state.getText().toString());
            person.setCountry(binding.country.getText().toString());
            person.setCovidStatus(binding.covid.getText().toString());
            person.setGender(binding.gender.getText().toString());

            DataBaseHelper.registerUser(person , new DataBaseHelper.Listener<Void>() {
                @Override
                public void onSuccess(Void unused){
                    Helper.myDetails = person;
                    afi.changeFragmentTo(new UserDashBoardFragment(), true);
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