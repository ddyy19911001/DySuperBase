package yin.deng.dysuperbase;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import yin.deng.superbase.activity.SuperBaseActivity;
import yin.deng.superbase.fragment.BasePagerAdapter;

public class MainActivity extends SuperBaseActivity {
    private FrameLayout fg;
    private TextView tvStart;

    @Override
    public void bindViewWithId() {
        fg = (FrameLayout) findViewById(R.id.fg);
        tvStart = (TextView) findViewById(R.id.tv_start_to);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initFirst() {
        MainFragment fragment=new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fg, fragment).commit();
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }
}

