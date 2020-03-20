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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class studentAdapter extends RecyclerView.Adapter<studentAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<myModels.studentModel> contacts;
    private String stat;
    private Context activity;
    private  OnItemClickListener mlistener;
    public studentAdapter(List<myModels.studentModel> contacts, Context context, OnItemClickListener listener){
        this.activity = context;
        this.inflater = LayoutInflater.from(context);
        this.mlistener = listener;
        this.contacts = contacts;
    }
    public interface OnItemClickListener{
        void onNameClick(View v, int position);
        void onMarkClick(View v, int position);
        void onImageClick(View v, int position);
    }
    public void setOnitemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customcompanystudent,parent,false);
        RecyclerHolder holder= new RecyclerHolder(view,mlistener);
        return holder;
    }
    int prevpos=0;
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        try {
            int g = holder.getAdapterPosition();
            myModels.studentModel contact = contacts.get(g);
            String applicationStatus = contact.getAppStatus();

            holder.mark.setVisibility(View.GONE);
            holder.clickApply.setVisibility(View.GONE);

            if( applicationStatus.equals("mark")){
                holder.mark.setVisibility(View.VISIBLE);
//                holder.studRegno.setVisibility(View.GONE);
//                holder.studDegree.setVisibility(View.GONE);
            }

            if(applicationStatus.equals("0")){
                holder.clickApply.setVisibility(View.VISIBLE);
            }

            holder.studName.setText(contact.getStudName());
            holder.studEmail.setText(contact.getStudEmail() + " / " + contact.getStudPhone());

//            holder.studFaculty.setText("Faculty Of " + contact.getStudFaculty() + " / " + contact.getStudDept());
//            holder.studRegno.setText( contact.getRegno() + " / "+ contact.getGender());
//            holder.studDegree.setText( contact.getItState() + " State / "+ contact.getItLgov());

            Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getProfilePic(), 0, contact.getProfilePic().length);
            holder.profilePics.setImageBitmap(bitmap);

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
        TextView studEmail,  studName;
//        TextView studRegno, studEmail, studFaculty, studName,studDegree;
        Button clickApply;
        CheckBox mark;
        ImageView profilePics;
        public RecyclerHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);

            studName =  itemView.findViewById(R.id.studName);
            studEmail =  itemView.findViewById(R.id.studEmail);
            clickApply = itemView.findViewById(R.id.btnApply);
            profilePics = itemView.findViewById(R.id.profile_pic);
            mark = itemView.findViewById(R.id.mark);
//            studName =  itemView.findViewById(R.id.studName);
//            studRegno = itemView.findViewById(R.id.studRegno);
//            studEmail =  itemView.findViewById(R.id.studEmail);
//            studFaculty = itemView.findViewById(R.id.studFaculty);
//            clickApply = itemView.findViewById(R.id.btnApply);
//            studDegree = itemView.findViewById(R.id.studDegree);
//            profilePics = itemView.findViewById(R.id.profile_pic);
//            mark = itemView.findViewById(R.id.mark);

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

            mark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onMarkClick(view, position);
                        }
                    }
                }
            });

            profilePics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onImageClick(view, position);
                        }
                    }
                }
            });

        }
    }

    public void setFilter(ArrayList<myModels.studentModel> newList){
        contacts = new ArrayList<>();
        contacts.addAll(newList);
        notifyDataSetChanged();
    }


}

