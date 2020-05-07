package com.example.textannotation.view.doTask.textMatch.section;

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
public class TextMatchAboveAdapter extends FragmentPagerAdapter {

    FragmentManager fm;
    private List<TextMatchAboveFragment> list;
    private ArrayList<Content> array;
    //private List<String> stringList;

    public TextMatchAboveAdapter(FragmentManager fm, List<TextMatchAboveFragment> list) {
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




   /* @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position).getView());
    }*/

   /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = container.findFocus();list.get(position).getView();
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(v, 0);
        return list.get(position);
    }*/

    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        PlaceholderFragment fragment = (PlaceholderFragment) super.instantiateItem(container, position);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + position;
    }*/
}






