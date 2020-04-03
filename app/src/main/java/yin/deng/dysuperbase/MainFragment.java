package yin.deng.dysuperbase;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import yin.deng.superbase.fragment.BasePagerAdapter;
import yin.deng.superbase.fragment.ViewPagerSuperBaseFragment;

public class MainFragment extends ViewPagerSuperBaseFragment {
    private ViewPager vp;


    @Override
    public void bindViewWithId(View view) {
        vp = (ViewPager) view.findViewById(R.id.vp);

    }



    @Override
    public int setContentView() {
        return R.layout.fg_main;
    }



    @Override
    public void init() {
        MyFragment myFragment1=new MyFragment();
        MyFragment myFragment2=new MyFragment();
        MyFragment myFragment3=new MyFragment();
        MyFragment myFragment4=new MyFragment();
        MyFragment myFragment5=new MyFragment();
        List<Fragment> fgs=new ArrayList<>();
        fgs.add(myFragment1);
        fgs.add(myFragment2);
        fgs.add(myFragment3);
        fgs.add(myFragment4);
        fgs.add(myFragment5);
        BasePagerAdapter pagerAdapter=new BasePagerAdapter(getChildFragmentManager(),fgs);
        vp.setOffscreenPageLimit(5);
        vp.setAdapter(pagerAdapter);
    }

    @Override
    public void loadDataFirst() {
        super.loadDataFirst();
    }


}
