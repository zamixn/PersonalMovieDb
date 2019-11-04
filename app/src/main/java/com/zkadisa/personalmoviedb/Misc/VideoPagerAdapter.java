package com.zkadisa.personalmoviedb.Misc;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoPagerAdapter extends FragmentPagerAdapter {

    private List<String> mFragmentTitleList = new ArrayList<>();
    private List<String> mFragmentURLList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private final Context mContext;

    public VideoPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(String title, String url) {
        int position = mFragmentList.size();
        Fragment fragment = VideoPageFragment.newInstance(position + 1, title, url);
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);
        mFragmentURLList.add(position, url);
    }

    public void removeFragment(int position) {
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
        mFragmentURLList.remove(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
