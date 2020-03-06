package com.example.abuadit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class companyAdapter extends RecyclerView.Adapter<companyAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<myModels.itfOffice> contacts;
    private String stat;
    private Context activity;
    private  OnItemClickListener mlistener;
    public companyAdapter(List<myModels.itfOffice> contacts, Context context, OnItemClickListener listener){
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
            myModels.itfOffice contact = contacts.get(g);
            holder.companyName.setText(contact.getAreaOffice());
            holder.State.setText(contact.getState() + " State.");
            holder.ContactAdd.setText("Contact Add. : " + contact.getContactAdd());
            holder.Email.setText("Email Add. : " + contact.getEmail());
            holder.PhoneNo.setText("Phone No. : " + contact.getPhoneNo());

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
        TextView companyName, PhoneNo,State,Email,ContactAdd;
        public RecyclerHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            companyName =  itemView.findViewById(R.id.companyName);
            PhoneNo = itemView.findViewById(R.id.PhoneNo);
            Email =  itemView.findViewById(R.id.Email);
            State = itemView.findViewById(R.id.State);
            ContactAdd = itemView.findViewById(R.id.ContactAdd);

//            PhoneNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(listener!=null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            listener.onNameClick(view, position);
//                        }
//                    }
//                }
//            });

        }
    }

    public void setFilter(ArrayList<myModels.itfOffice> newList){
        contacts = new ArrayList<>();
        contacts.addAll(newList);
        notifyDataSetChanged();
    }


}
