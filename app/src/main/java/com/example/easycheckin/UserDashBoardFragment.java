package com.example.easycheckin;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.classes.VisitsData;
import com.example.easycheckin.databinding.FragmentUserDashBoardBinding;

import java.text.MessageFormat;
import java.util.List;

public class UserDashBoardFragment extends Fragment {

    private AFI afi;

    public UserDashBoardFragment( ){
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
        return inflater.inflate(R.layout.fragment_user_dash_board , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState){
        super.onViewCreated(view , savedInstanceState);
        FragmentUserDashBoardBinding binding = FragmentUserDashBoardBinding.bind(view);

        InstVisitAdapter adapter = new InstVisitAdapter(index -> {
            // click
            Bundle bundle = new Bundle();
            bundle.putInt(DetailsFragment.TYPE, DetailsFragment.INST_DATA);
            bundle.putInt(DetailsFragment.DATA, index);
            Fragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            afi.changeFragmentTo(fragment, false);
        });
        binding.itemsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.itemsList.setAdapter(adapter);

        binding.profile.setText(MessageFormat.format("{0}" , Helper.myDetails.getName().charAt(0)));
        binding.profile.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(DetailsFragment.TYPE, DetailsFragment.MY_USER_PROFILE);
            Fragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            afi.changeFragmentTo(fragment, false);
        });
        binding.scan.setOnClickListener(view1 -> afi.startScanner());

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.noVisits.setVisibility(View.INVISIBLE);
        DataBaseHelper.fetchVisitedPlaces(Helper.email , new DataBaseHelper.Listener<List<VisitsData<Institute>>>() {
            @Override
            public void onSuccess(List<VisitsData<Institute>> visitsData){
                if(visitsData.isEmpty()){
                    binding.noVisits.setVisibility(View.VISIBLE);
                }
                adapter.setDataList(visitsData);
                adapter.notifyDataSetChanged();
                Helper.instituteVisitsData = visitsData;
                binding.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(String message){
                System.out.println(message);
            }
        });
    }
}