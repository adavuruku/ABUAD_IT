package com.example.abuadit;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class studentAttendanceAdapter extends RecyclerView.Adapter<studentAttendanceAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<myModels.attendanceModel> contacts;
    private String stat;
    private Context activity;
    private  OnItemClickListener mlistener;
    public studentAttendanceAdapter(List<myModels.attendanceModel> contacts, Context context, OnItemClickListener listener){
        this.activity = context;
        this.inflater = LayoutInflater.from(context);
        this.mlistener = listener;
        this.contacts = contacts;
    }
    public interface OnItemClickListener{
    }
    public void setOnitemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customattendance,parent,false);
        RecyclerHolder holder= new RecyclerHolder(view,mlistener);
        return holder;
    }
    int prevpos=0;
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        try {
            int g = holder.getAdapterPosition();
            myModels.attendanceModel contact = contacts.get(g);
            String applicationStatus = contact.getAttendanceStatus();

            if( applicationStatus.equals("0")){
                holder.present.setText("Absent");
                holder.present.setTextColor(activity.getResources().getColor(R.color.colorAccent));
            }else{
                holder.present.setText("Present");
                holder.present.setTextColor(activity.getResources().getColor(R.color.colorGreen));
            }

            holder.studName.setText(contact.getStudname());
            holder.dateAttendance.setText(contact.getAttendanceDate());

            if (position > prevpos) {
                //you are scrooling down
                AnimationUtils.animate(holder, true);
            } else {
                //no you are scroolingup
                AnimationUtils.animate(holder, false);
            }
            prevpos = position;
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    //create the holder class
    class RecyclerHolder extends RecyclerView.ViewHolder{
        //the view items send here is from custom_row and is received here as itemView
        TextView studName, dateAttendance, present;

        public RecyclerHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            studName =  itemView.findViewById(R.id.studName);
            dateAttendance =  itemView.findViewById(R.id.dateAttendance);
            present = itemView.findViewById(R.id.present);

        }
    }

    public void setFilter(ArrayList<myModels.attendanceModel> newList){
        contacts = new ArrayList<>();
        contacts.addAll(newList);
        notifyDataSetChanged();
    }


}

