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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class InstVisitAdapter extends RecyclerView.Adapter<InstVisitAdapter.VisitItemVH> {

    private List<VisitsData<Institute>> dataList;
    private final ClickListener clickListener;

    public InstVisitAdapter(ClickListener clickListener){
        this.dataList = new ArrayList<>();
        this.clickListener = clickListener;
    }

    public void setDataList(List<VisitsData<Institute>> dataList){
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
        VisitsData<Institute> visitsData = dataList.get(position);
        holder.binding.date.setText(visitsData.getVisits().getDate());
        holder.binding.time.setText(visitsData.getVisits().getTime());
        holder.binding.name.setText(visitsData.getData().getName());
        holder.binding.mobileOrAddress.setText(MessageFormat.format("{0}, {1}" , visitsData.getData().getAddress() , visitsData.getData().getCity()));
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
