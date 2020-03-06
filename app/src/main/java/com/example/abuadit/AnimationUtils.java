package com.example.abuadit;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sherif146 on 04/01/2018.
 */

public class AnimationUtils {

    public static void animate(RecyclerView.ViewHolder holder, boolean goinDown){
        ObjectAnimator animatorTransalateY;
        if(goinDown){
             animatorTransalateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",200,0);
        }else{
             animatorTransalateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",-200,0);
        }
        animatorTransalateY.setDuration(1000);
        animatorTransalateY.start();
    }

    //another for of animation using groupanimation ..animationset
    public static void animategroup(RecyclerView.ViewHolder holder,boolean goinDown){
        ObjectAnimator animatorTransalateY,animatorTransalateX;
        if(goinDown){
            animatorTransalateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",200,0);
        }else{
            animatorTransalateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",-200,0);
        }
        animatorTransalateX = ObjectAnimator.ofFloat(holder.itemView,"translationX",-50,50,-30,30,-20,20,-5,5,0);
        animatorTransalateY.setDuration(1000);
        animatorTransalateX.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorTransalateY,animatorTransalateX);
        animatorSet.setInterpolator(new AnticipateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
    public static void imagegroup(ImageView holder, boolean goinDown){
        ObjectAnimator animatorTransalateY,animatorTransalateX;
        if(goinDown){
            animatorTransalateY = ObjectAnimator.ofFloat(holder,"translationY",200,0);
        }else{
            animatorTransalateY = ObjectAnimator.ofFloat(holder,"translationY",-200,0);
        }
        animatorTransalateX = ObjectAnimator.ofFloat(holder,"translationX",-50,50,-30,30,-20,20,-5,5,0);
        animatorTransalateY.setDuration(1000);
        animatorTransalateX.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorTransalateY,animatorTransalateX);
        animatorSet.setInterpolator(new AnticipateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
    //toolbar animation
    public static void animatetoolbar(Toolbar toolbar){
        /**toolbar.setRotationX(-90);
        toolbar.setAlpha(0.2f);
        toolbar.setPivotX(0.0f);
        toolbar.setPivotY(0.0f);**/
        Animator alpha = ObjectAnimator.ofFloat(toolbar,"alpha",0.2F,0.4F,0.6F,0.8F,1.0F).setDuration(1000);
        Animator rotationX = ObjectAnimator.ofFloat(toolbar,"rotationX",-90,60,-45,45,-10,30,0,20,0,5,0).setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(alpha,rotationX);
        animatorSet.start();

    }
}
