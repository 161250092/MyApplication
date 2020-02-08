package com.example.kongmin.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;

import com.example.kongmin.pojo.Content;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    FragmentManager fm;

    private List<TextCategoryActivity.PlaceholderFragment> list;
    private ArrayList<Content> array;
    //private List<String> stringList;

    public SectionsPagerAdapter(FragmentManager fm, List<TextCategoryActivity.PlaceholderFragment> list) {
        super(fm);
        this.fm=fm;
        this.list = list;

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return list.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return list.size();
    }




    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position).getView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = list.get(position).getView();
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(v, 0);
        return list.get(position);
    }

    /*@Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return array.get(position).getContentname();
    }*/

}






