package com.example.easycheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.classes.Person;
import com.example.easycheckin.classes.VisitsData;
import com.example.easycheckin.databinding.VisitCardBinding;

import java.util.ArrayList;
import java.util.List;

public class UserVisitsAdapter extends RecyclerView.Adapter<UserVisitsAdapter.VisitItemVH> {

    private List<VisitsData<Person>> dataList;
    private final ClickListener clickListener;

    public UserVisitsAdapter(ClickListener clickListener){
        this.dataList = new ArrayList<>();
        this.clickListener = clickListener;
    }

    public void setDataList(List<VisitsData<Person>> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public VisitItemVH onCreateViewHolder(@NonNull ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_card, parent, false);
        return new VisitItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitItemVH holder , int position){
        VisitsData<Person> visitsData = dataList.get(position);
        holder.binding.date.setText(visitsData.getVisits().getDate());
        holder.binding.time.setText(visitsData.getVisits().getTime());
        holder.binding.name.setText(visitsData.getData().getName());
        holder.binding.mobileOrAddress.setText(visitsData.getData().getMobileNo());
    }

    @Override
    public int getItemCount( ){
        return dataList.size();
    }

    class VisitItemVH extends RecyclerView.ViewHolder{

        VisitCardBinding binding;

        public VisitItemVH(@NonNull View itemView){
            super(itemView);
            binding = VisitCardBinding.bind(itemView);

            itemView.setOnClickListener(view -> {
                clickListener.onClick(getAdapterPosition());
            });
        }
    }

    public interface ClickListener{
        void onClick(int index);
    }
}
