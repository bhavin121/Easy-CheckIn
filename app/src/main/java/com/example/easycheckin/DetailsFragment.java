package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.classes.Person;
import com.example.easycheckin.classes.VisitsData;
import com.example.easycheckin.databinding.FragmentDetailsBinding;

import java.text.MessageFormat;

public class DetailsFragment extends Fragment {

    public static final int MY_USER_PROFILE = 0;
    public static final int MY_INST_PROFILE = 1;
    public static final int USER_DATA = 2;
    public static final int INST_DATA = 3;

    public static final String TYPE = "type";
    public static final String DATA = "data";
    private AFI afi;

    public DetailsFragment( ){
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
        return inflater.inflate(R.layout.fragment_details , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        FragmentDetailsBinding binding = FragmentDetailsBinding.bind(view);

        binding.logOut.setOnClickListener(view1 -> afi.signOut());

        int index;
        Bundle bundle = getArguments();
        if(bundle != null){
            index = bundle.getInt(DATA);
            switch(bundle.getInt(TYPE)){
                case MY_INST_PROFILE:
                    binding.uOrI.setImageResource(R.drawable.ic_office);
                    binding.covid.setVisibility(View.GONE);
                    binding.dateTime.setVisibility(View.GONE);
                    Institute institute = Helper.myInstitute;
                    binding.name.setText(institute.getName());
                    binding.details.setText(institute.getInstitutionType());
                    binding.email.setText(institute.getEmail());
                    binding.mobile.setText(institute.getMobileNo());
                    binding.address.setText(institute.completeAddress());
                    break;
                case MY_USER_PROFILE:
                    binding.dateTime.setVisibility(View.GONE);
                    Person person = Helper.myDetails;
                    binding.name.setText(person.getName());
                    binding.details.setText(MessageFormat.format("{0} {1}" , person.getAge() , person.getGender().charAt(0)));
                    binding.email.setText(person.getEmail());
                    binding.mobile.setText(person.getMobileNo());
                    binding.address.setText(person.completeAddress());
                    binding.covid.setText(person.getCovidStatus());
                    break;
                case INST_DATA:
                    VisitsData<Institute> visitsData = Helper.instituteVisitsData.get(index);
                    binding.name.setText(visitsData.getData().getName());
                    binding.logOut.setVisibility(View.GONE);
                    binding.covid.setVisibility(View.GONE);
                    binding.uOrI.setImageResource(R.drawable.ic_office);
                    binding.address.setText(visitsData.getData().completeAddress());
                    binding.mobile.setText(visitsData.getData().getMobileNo());
                    binding.email.setText(visitsData.getData().getEmail());
                    binding.dateTime.setText(MessageFormat.format("{0} {1}" , visitsData.getVisits().getTime() , visitsData.getVisits().getDate()));
                    binding.details.setText(visitsData.getData().getInstitutionType());
                    break;
                case USER_DATA:
                    VisitsData<Person> visitsData1 = Helper.personVisitsData.get(index);
                    binding.name.setText(visitsData1.getData().getName());
                    binding.logOut.setVisibility(View.GONE);
                    binding.covid.setText(visitsData1.getData().getCovidStatus());
                    binding.uOrI.setImageResource(R.drawable.ic_office);
                    binding.address.setText(visitsData1.getData().completeAddress());
                    binding.mobile.setText(visitsData1.getData().getMobileNo());
                    binding.email.setText(visitsData1.getData().getEmail());
                    binding.dateTime.setText(MessageFormat.format("{0} {1}" , visitsData1.getVisits().getTime() , visitsData1.getVisits().getDate()));
                    binding.details.setText(MessageFormat.format("{0} {1}", visitsData1.getData().getAge(), visitsData1.getData().getGender().charAt(0)));
                    break;
            }
        }

    }
}