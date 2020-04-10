package com.bankbros.ldaadmin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HoldersRecyclerViewAdapter extends RecyclerView.Adapter<HoldersRecyclerViewAdapter.UsersViewHolder> {



    private ArrayList<RegisteredUsersModel> mItemsList;
    private int type;
    private Context context;

    private OnTeacherClickListener mListener;

    public interface OnTeacherClickListener {
        void onHolderClicked(int Position);
        void onHolderSelected(int Position);
    }


    public void setOnTeacherClickListener(OnTeacherClickListener listener){
        mListener = listener;
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{


        public TextView mUserName;
        public TextView mUserEmail;
        public TextView mRegisterDate;
        public CardView mActive;


        public UsersViewHolder(@NonNull final View itemView, final OnTeacherClickListener listener) {
            super(itemView);

            mUserName=itemView.findViewById(R.id.tv_user_name);
            mUserEmail=itemView.findViewById(R.id.tv_user_email);
            mRegisterDate=itemView.findViewById(R.id.tv_user_date);
            mActive =itemView.findViewById(R.id.cv_show_active);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onHolderClicked(position);
                        }
                    }
                }
            });
        }

    }


    public HoldersRecyclerViewAdapter(Context context,ArrayList<RegisteredUsersModel> itemsList, int type){

       this.mItemsList =  new ArrayList<>();
       mItemsList =itemsList;
       this.context = context;
       this.type = type;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_registered_users,viewGroup,false);
        UsersViewHolder viewHolder = new UsersViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersViewHolder UsersViewHolder, int i) {

        final RegisteredUsersModel current_item  = mItemsList.get(i);
        
        UsersViewHolder.mUserName.setText(current_item.getUserName());
        UsersViewHolder.mUserEmail.setText(current_item.getUserEmail());
        UsersViewHolder.mRegisterDate.setText(current_item.getDateRegistered());

       if(type == 1) {
//           Toast.makeText(context, "Sorting ", Toast.LENGTH_SHORT).show();
           if (current_item.getNotificationcount() > 0) {
               UsersViewHolder.mActive.setCardBackgroundColor(Color.RED);
           }
           else UsersViewHolder.mActive.setCardBackgroundColor(Color.WHITE);
       }
       else {
           UsersViewHolder.mActive.setVisibility(View.INVISIBLE);

       }

        if(current_item.isUserEnabled())
        {
           UsersViewHolder.mUserName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        else {
            UsersViewHolder.mUserName.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

}
