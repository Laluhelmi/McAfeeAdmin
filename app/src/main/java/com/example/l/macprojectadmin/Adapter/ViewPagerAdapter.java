package com.example.l.macprojectadmin.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.l.macprojectadmin.Fragment.FragIklan;

import java.io.File;
import java.util.List;

/**
 * Created by L on 12/01/18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int banyak;
    private List<String> files;

    public ViewPagerAdapter(FragmentManager manager,int banyak,List<String> files){
        super(manager);
        this.files = files;
        this.banyak = banyak;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FragIklan();
        Bundle bundle = new Bundle();
        bundle.putString("gambarpath",files.get(position));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return banyak;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Iklan "+String.valueOf(position+1);
    }
}
