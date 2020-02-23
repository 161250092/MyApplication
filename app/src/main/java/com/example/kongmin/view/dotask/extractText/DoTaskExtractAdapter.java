package com.example.kongmin.view.doTask.extractText;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.kongmin.pojo.Content;

import java.util.ArrayList;
import java.util.List;

public class DoTaskExtractAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private List<DoTaskExtractFragment> list;
    private ArrayList<Content> array;
    //private List<String> stringList;

    public DoTaskExtractAdapter(FragmentManager fm, List<DoTaskExtractFragment> list) {
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



    //新添加的，解决数据错乱的问题
    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

}






