package com.example.financialportfoliomanagement.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.financialportfoliomanagement.Fragments.IndexFragment;
import com.example.financialportfoliomanagement.Fragments.StockFragment;

public class DashBoardFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DashBoardFragmentAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                IndexFragment tab1 = new IndexFragment();
                return tab1;
            case 1:
                StockFragment tab2 = new StockFragment();
                return tab2;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
