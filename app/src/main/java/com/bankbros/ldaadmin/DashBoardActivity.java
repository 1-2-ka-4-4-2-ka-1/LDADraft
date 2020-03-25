package com.bankbros.ldaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                case 1:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                    break;
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
        Adapter.add(new TransactionsFragment(), "Transactions");
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

                case R.id.navigation_profile:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_chat:
                    viewPager.setCurrentItem(2);
                    return true;
            }


            return false;
        }
    };


}
