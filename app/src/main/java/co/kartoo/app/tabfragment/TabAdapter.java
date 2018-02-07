package co.kartoo.app.tabfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.kartoo.app.category.FragmentCategory_;

public class TabAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 4;
    //public String tabTitles[] = new String[] { "Home", "Categories", "Nearby", "Social"};
    public String tabTitles[];
    final int POSITION_HOME = 0;
    final int POSITION_CATEGORY = 1;
    final int POSITION_NEARBY = 2;
    final int POSITION_MALLS = 3;

    int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITION_HOME : return new FragmentHome_();
            case POSITION_CATEGORY : return new FragmentCategory_();
            case POSITION_NEARBY : return new FragmentNearby_();
            case POSITION_MALLS : return new FragmentMalls_();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
/*
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
    */
}
