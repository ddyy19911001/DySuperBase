package yin.deng.dysuperbase;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import yin.deng.superbase.activity.SuperBaseActivity;
import yin.deng.superbase.fragment.BasePagerAdapter;

public class MainActivity extends SuperBaseActivity {
    private FrameLayout fg;
    @Override
    public void bindViewWithId() {
        fg = (FrameLayout) findViewById(R.id.fg);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initFirst() {
        MainFragment fragment=new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fg, fragment).commit();
    }
}

