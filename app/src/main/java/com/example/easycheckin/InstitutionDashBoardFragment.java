package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.easycheckin.classes.Person;
import com.example.easycheckin.classes.VisitsData;
import com.example.easycheckin.databinding.FragmentInstitutionDashBoardBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.List;

public class InstitutionDashBoardFragment extends Fragment {

    private AFI afi;
    private UserVisitsAdapter adapter;
    private FragmentInstitutionDashBoardBinding binding;

    public InstitutionDashBoardFragment( ){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_institution_dash_board , container , false);
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
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        binding = FragmentInstitutionDashBoardBinding.bind(view);

        adapter = new UserVisitsAdapter(index -> {
            // click on the card
            Bundle bundle = new Bundle();
            bundle.putInt(DetailsFragment.TYPE, DetailsFragment.USER_DATA);
            bundle.putInt(DetailsFragment.DATA, index);
            Fragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            afi.changeFragmentTo(fragment, false);
        });
        binding.itemsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.itemsList.setAdapter(adapter);

        binding.profile.setText(String.valueOf(Helper.myInstitute.getName().charAt(0)));
        binding.profile.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(DetailsFragment.TYPE, DetailsFragment.MY_INST_PROFILE);
            Fragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            afi.changeFragmentTo(fragment, false);
        });

        binding.qrcode.setOnClickListener(view1 -> afi.changeFragmentTo(new GenerateQRFragment(), false));

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose Date")
                .setCalendarConstraints(new CalendarConstraints.Builder()
                        .setEnd(Calendar.getInstance().getTimeInMillis())
                        .setValidator(new CalendarConstraints.DateValidator() {
                            @Override
                            public boolean isValid(long date){
                                return (Calendar.getInstance().getTimeInMillis()>=date);
                            }

                            @Override
                            public int describeContents( ){
                                return 0;
                            }

                            @Override
                            public void writeToParcel(Parcel parcel , int i){

                            }
                        })
                        .build())
                .build();

        binding.date.setOnClickListener(view1 -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        String date = Helper.getDate();
        binding.date.setText(date);
        fetchData(date);

        datePicker.addOnPositiveButtonClickListener(selection -> {
            binding.date.setText(datePicker.getHeaderText());
            fetchData(datePicker.getHeaderText());
        });
    }

    public void fetchData(String date){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.noVisits.setVisibility(View.INVISIBLE);
        DataBaseHelper.fetchUsersVisited(Helper.email , date,new DataBaseHelper.Listener<List<VisitsData<Person>>>() {
            @Override
            public void onSuccess(List<VisitsData<Person>> visitsData){
                if(visitsData.isEmpty()){
                    binding.noVisits.setVisibility(View.VISIBLE);
                }

                adapter.setDataList(visitsData);
                adapter.notifyDataSetChanged();
                Helper.personVisitsData = visitsData;
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String message){
                System.out.println(message);
            }
        } );
    }
}