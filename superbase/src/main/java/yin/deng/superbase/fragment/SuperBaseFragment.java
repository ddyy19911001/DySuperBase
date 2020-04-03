package yin.deng.superbase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import yin.deng.superbase.activity.ToastUtil;


/**
 * 这个fragment是android.V4中的fragment不是android.app包里面的Fragment
 */
public abstract class SuperBaseFragment extends Fragment {
    public Activity mActivity;
    public static final String TAG = ViewPagerSuperBaseFragment.class.getSimpleName();
    public boolean mHaveLoadData; // 表示是否已经请求过数据
    public View mRootView;

    // 表示找控件完成, 给控件们设置数据不会报空指针了
    public boolean mViewInflateFinished;
    public ToastUtil toast;
    public boolean isThisVisible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (mRootView != null) {
            return mRootView;
        }
        mRootView = inflater.inflate(setContentView(), container, false);
        bindViewWithId(mRootView);
        mViewInflateFinished = true;
        init();
        return mRootView;
    }


    public void bindViewWithId(View view) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint, isVisibleToUser = " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(isThisVisible!=isVisibleToUser){
            isThisVisible=isVisibleToUser;
            onFragmentVisibleChange(isThisVisible);
        }
        // 如果还没有加载过数据 && 用户切换到了这个fragment
        // 那就开始加载数据
        if (!mHaveLoadData && isVisibleToUser) {
            loadDataFirst();
            mHaveLoadData = true;
            return;
        }
        if(isNeedRefreshAllTime()&&mHaveLoadData&&isVisibleToUser){
            refreshData();
        }
    }





    /**
     *
     * @param isVisible  true不可见到可见  false 可见到不可见
     */
    public void onFragmentVisibleChange(boolean isVisible){

    }

    /**
     * 如果需要每次都刷新数据，请先设置isNeedRefreshAllTime为true
     */
    public void refreshData(){

    }

    /**
     * 首次加载数据，此时还未绑定控件id
     */
    public  void loadDataFirst(){

    }


    /***
     * 是否需要每次显示的时候都刷新数据
     * @return
     */
    public boolean isNeedRefreshAllTime() {
        return false;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity=getActivity();
    }


    public abstract int setContentView();


    public boolean isFragmentVisible() {
        return isThisVisible;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity=getActivity();
    }



    public void showTs(String msg) {
        if (toast == null) {
            toast = new ToastUtil(getActivity(),msg);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }




    public View getRootView(){
        return mRootView;
    }

    /**
     * 初始化数据可写在这里
     */
    public abstract void init();

}
