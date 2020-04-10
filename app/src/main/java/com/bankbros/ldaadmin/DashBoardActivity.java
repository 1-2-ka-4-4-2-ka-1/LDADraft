package com.bankbros.ldaadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashBoardActivity extends AppCompatActivity {


    public static ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        viewPager = findViewById(R.id.page_container);
        setupFm(getSupportFragmentManager(), viewPager);

        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(mOnPageChangeListener);


        //bottom Navigation View
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("Message Notifications", "Message", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        setNotificationListener();


    }


    private void setNotificationListener(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
//        reference.child("users").setValue(null);
        reference.child("Admin").child("Notifications").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.i("New Message Received", "onChildAdded: "+dataSnapshot.getValue());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            switch (i) {
//                case 0:
//                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
//                    break;
//                case 1:
//                    bottomNavigationView.setSelectedItemId(R.id.navigation_previous);
//                    break;
                case 0:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_search_edit);
                    break;
//                case 1:
//                    bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
//                    break;
                case 2:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_chat);
                    break;

            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager) {

        PageChangeAdapter Adapter = new PageChangeAdapter(fragmentManager);

//        Adapter.add(new HomeFragment(), "Home");
//        Adapter.add(new PreviousNoticesFragment(), "Notices");
        Adapter.add(new RegisteredUsersFragment(), "Users");
//        Adapter.add(new TransactionsFragment(), "Transactions");
        Adapter.add(new ChatsFragment(), "Chat");


        viewPager.setAdapter(Adapter);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
//                case R.id.navigation_home:
//                    viewPager.setCurrentItem(0);
//                    return true;
//                case R.id.navigation_previous:
//                    viewPager.setCurrentItem(1);

//                    return true;
                case R.id.navigation_search_edit:
                    viewPager.setCurrentItem(0);
                    return true;

//                case R.id.navigation_profile:
//                    viewPager.setCurrentItem(1);
//                    return true;
                case R.id.navigation_chat:
                    viewPager.setCurrentItem(2);
                    return true;
            }


            return false;
        }
    };


}
