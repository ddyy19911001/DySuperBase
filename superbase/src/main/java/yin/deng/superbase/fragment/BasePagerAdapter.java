package yin.deng.superbase.fragment;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class BasePagerAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<Fragment> fgs;

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fgs, List<String> titles) {
        super(fm);
        this.fgs=fgs;
        this.titles=titles;
    }

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fgs) {
        super(fm);
        this.fgs=fgs;
        this.titles=null;
    }


    @Override
    public Fragment getItem(int position) {
        return fgs.get(position);
    }

    @Override
    public int getCount() {
        return fgs.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titles==null){
            return null;
        }
        return titles.get(position);
    }

}
