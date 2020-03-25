package com.bankbros.ldaadmin;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PageChangeAdapter extends FragmentPagerAdapter {


    private List<androidx.fragment.app.Fragment> Fragment = new ArrayList<>();
    private List<String> NameFragment = new ArrayList<>();

    public PageChangeAdapter(FragmentManager fm) {
        super(fm);

    }


    public void add(Fragment fragment,String fragmentName){
        Fragment.add(fragment);
        NameFragment.add(fragmentName);

    }

    @Override
    public Fragment getItem(int i) {

       return Fragment.get(i);

        }

    @Override
    public int getCount() {
        return 3;
    }
}
