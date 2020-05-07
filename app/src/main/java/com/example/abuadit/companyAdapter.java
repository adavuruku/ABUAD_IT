package com.example.abuadit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class companyAdapter extends RecyclerView.Adapter<companyAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<myModels.companyModel> contacts;
    private String stat;
    private Context activity;
    private  OnItemClickListener mlistener;
    public companyAdapter(List<myModels.companyModel> contacts, Context context, OnItemClickListener listener){
        this.activity = context;
        this.inflater = LayoutInflater.from(context);
        this.mlistener = listener;
        this.contacts = contacts;
    }
    public interface OnItemClickListener{
        void onNameClick(View v, int position);
    }
    public void setOnitemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customcompany,parent,false);
        RecyclerHolder holder= new RecyclerHolder(view,mlistener);
        return holder;
    }
    int prevpos=0;
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        try {
            int g = holder.getAdapterPosition();
            myModels.companyModel contact = contacts.get(g);
            String applicationStatus = contact.getAppStatus();
            if( applicationStatus==""){
                holder.appStatus.setVisibility(View.GONE);
            }else{

                if( applicationStatus.equals("1")){
                    holder.appStatus.setVisibility(View.VISIBLE);
                    holder.appStatus.setText("Company Has Approved Your Application");
                    holder.appStatus.setTextColor(activity.getResources().getColor(R.color.colorGreen));
                    holder.clickApply.setText("ACCEPT OFFER");
                }else if(applicationStatus.equals("2")){
                    holder.appStatus.setVisibility(View.VISIBLE);
                    holder.appStatus.setText("You Have Accept Application");
                    holder.appStatus.setTextColor(activity.getResources().getColor(R.color.colorGreen));
                    holder.clickApply.setVisibility(View.GONE);
                }else {
                    holder.appStatus.setVisibility(View.VISIBLE);
                    holder.appStatus.setText("Pending Application");
                    holder.appStatus.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                    holder.clickApply.setVisibility(View.GONE);
                }
            }
            holder.companyName.setText(contact.getCompanyName());
            holder.State.setText(contact.getLgov() + " / " + contact.getState() + " State.");
            holder.ContactAdd.setText("Contact Add. : " + contact.getContactAdd());
            holder.Description.setText("Description. : " + contact.getDescription());
            holder.PhoneNo.setText(contact.getPhoneNo() + " / " + contact.getEmail());

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
        TextView companyName, PhoneNo,State,Description,ContactAdd,appStatus;
        Button clickApply;
        public RecyclerHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            companyName =  itemView.findViewById(R.id.companyName);
            PhoneNo = itemView.findViewById(R.id.PhoneNo);
            Description =  itemView.findViewById(R.id.description);
            State = itemView.findViewById(R.id.State);
            ContactAdd = itemView.findViewById(R.id.ContactAdd);
            clickApply = itemView.findViewById(R.id.btnApply);
            appStatus = itemView.findViewById(R.id.appstatus);

            clickApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onNameClick(view, position);
                        }
                    }
                }
            });

        }
    }

    public void setFilter(ArrayList<myModels.companyModel> newList){
        contacts = new ArrayList<>();
        contacts.addAll(newList);
        notifyDataSetChanged();
    }


}
