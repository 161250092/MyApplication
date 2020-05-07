package com.example.textannotation.view.doTask.textCategory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.textannotation.pojo.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TextCategoryAdapter extends FragmentPagerAdapter {

    FragmentManager fm;
    private List<TextCategoryFragment> list;
    private ArrayList<Content> array;


    public TextCategoryAdapter(FragmentManager fm, List<TextCategoryFragment> list) {
        super(fm);
        this.fm=fm;
        this.list = list;

    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return list.size();
    }

    //新添加的，解决数据错乱的问题
    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }


}






