package yin.deng.dysuperbase;

import android.view.View;
import android.widget.TextView;

import yin.deng.superbase.activity.LogUtils;
import yin.deng.superbase.fragment.SuperBaseFragment;
import yin.deng.superbase.fragment.ViewPagerSuperBaseFragment;

public class MyFragment extends ViewPagerSuperBaseFragment {
    private TextView tvText;
    private int time=0;
    public String nowData;
    @Override
    public int setContentView() {
        return R.layout.test_fg;
    }

    @Override
    public void bindViewWithId(View view) {
        tvText = (TextView) view.findViewById(R.id.tv_text);
        tvText.setText(nowData);
    }

    @Override
    public void init() {

    }


    @Override
    public void onFragmentVisibleChange(boolean isVisible) {
        LogUtils.i("是否可见："+isVisible);
    }

    @Override
    public void loadDataFirst() {
        nowData="我是首次需要加载的数据";
        if(tvText!=null){
            tvText.setText(nowData);
        }
    }

    @Override
    public void refreshData() {
        nowData="我是刷新后的数据"+time;
        tvText.setText(nowData);
        time++;
    }


    @Override
    public boolean isNeedRefreshAllTime() {
        return false;
    }
}
