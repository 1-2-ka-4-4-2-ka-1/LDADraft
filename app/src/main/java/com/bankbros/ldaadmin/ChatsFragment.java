package com.bankbros.ldaadmin;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }

    private EditText searchHolders;
    private RecyclerView usersRecyclerView;
    private LinearLayoutManager layoutManager;
    private HoldersRecyclerViewAdapter usersRecyclerViewAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ArrayList<RegisteredUsersModel> usersArrayList;
    private ArrayList<String> selectedHolders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);


        searchHolders = v.findViewById(R.id.ed_search_users);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();



        usersArrayList = new ArrayList<>();
        selectedHolders = new ArrayList<>();
        usersRecyclerView = v.findViewById(R.id.rv_account_holders);
        layoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerViewAdapter = new HoldersRecyclerViewAdapter(getActivity(),usersArrayList,1);
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRecyclerView.setAdapter(usersRecyclerViewAdapter);


        usersRecyclerViewAdapter.setOnTeacherClickListener(new HoldersRecyclerViewAdapter.OnTeacherClickListener() {
            @Override
            public void onHolderClicked(int Position) {
                Intent intent = new Intent(getContext(),ChatsActivity.class);
                intent.putExtra("obj",usersArrayList.get(Position));
                usersArrayList.get(Position).setNotificationcount(0);
                usersRecyclerViewAdapter.notifyItemChanged(Position);

                databaseReference.child("Admin").child("usersdata").child(usersArrayList.get(Position).getUserId()).child("notificationcount").setValue(0);

                startActivity(intent);
            }

            @Override
            public void onHolderSelected(int Position) {

            }
        });




        searchHolders.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                setAdapter(s.toString());
            }
        });

        setAdapter("");

        databaseReference.child("Admin").child("usersdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setAdapter("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    public void setAdapter(final  String val){


        final int  searchLimit = 15;




        databaseReference.child("Admin").child("usersdata").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersArrayList.clear();
                int Counter = 0;


                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    if(!val.trim().equals("")){
                        if(Objects.requireNonNull(userSnapShot.child("userEmail").getValue()).toString().toLowerCase().contains(val.toLowerCase())  || userSnapShot.child("userName").getValue().toString().contains(val)){

                            Log.i("====", "onDataChange: "+ Objects.requireNonNull(userSnapShot.child("userEmail").getValue()).toString());
                            RegisteredUsersModel  holderSearchModel = (userSnapShot.getValue(RegisteredUsersModel.class));
                            usersArrayList.add(holderSearchModel);
                            Counter++;

                        }}else {

                        RegisteredUsersModel  holderSearchModel = (userSnapShot.getValue(RegisteredUsersModel.class));
                        usersArrayList.add(holderSearchModel);
                        Log.i("====", "onDataChange: ");
                    }

                    if(Counter == 15 && searchLimit != -1)
                        break;

                }
                Collections.sort(usersArrayList,new CustomComparator());
                usersRecyclerViewAdapter.notifyDataSetChanged();
//                Log.i("sorting", "onDataChange: "+usersArrayList.get(0).getNotificationcount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class CustomComparator implements Comparator<RegisteredUsersModel> {
        @Override
        public int compare(RegisteredUsersModel o1, RegisteredUsersModel o2) {
            return (o2.getNotificationcount())-(o1.getNotificationcount());
        }
    }
}
