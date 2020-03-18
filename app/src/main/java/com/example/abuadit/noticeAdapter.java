package com.example.abuadit;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<myModels.notice> contacts;
    private String stat;
    private Context activity;
    private  OnItemClickListener mlistener;
    public noticeAdapter(List<myModels.notice> contacts, Context context, OnItemClickListener listener){
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
        View view = inflater.inflate(R.layout.customnotice,parent,false);
        RecyclerHolder holder= new RecyclerHolder(view,mlistener);
        return holder;
    }
    int prevpos=0;
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        try {
            int g = holder.getAdapterPosition();
            myModels.notice contact = contacts.get(g);
            holder.author.setText(contact.getAuthor());
            String notipart = TextUtils.substring(contact.getBody(),0,300).concat("...");
            holder.body.setText(notipart);
            holder.date.setText(contact.getDate());
            holder.title.setText(contact.getTitle());

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
        TextView title, author,date,body;
        CardView card;
        public RecyclerHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            title =  itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            author =  itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);

            card.setOnClickListener(new View.OnClickListener() {
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

    public void setFilter(ArrayList<myModels.notice> newList){
        contacts = new ArrayList<>();
        contacts.addAll(newList);
        notifyDataSetChanged();
    }


}
