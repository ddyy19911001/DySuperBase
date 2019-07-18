package yin.deng.dysuperbase;

import android.Manifest;

import java.util.List;

import yin.deng.superbase.activity.LogUtils;
import yin.deng.superbase.activity.SuperBaseActivity;

public class MainActivity extends SuperBaseActivity {
    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initFirst() {
        requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},this);
    }

    @Override
    public void onDenied(List<String> deniedPermission) {
        super.onDenied(deniedPermission);
        LogUtils.d("权限被拒绝");
    }

    @Override
    public void onGranted() {
        super.onGranted();
        LogUtils.d("权限申请成功");
    }


}

