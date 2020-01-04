package yin.deng.dysuperbase;

import android.Manifest;

import java.util.List;

import yin.deng.superbase.activity.LogUtils;
import yin.deng.superbase.activity.SuperBaseActivity;
import yin.deng.superbase.activity.permission.PermissionListener;

public class MainActivity extends SuperBaseActivity {
    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initFirst() {
        showTs("测试屏幕啊啊啊啊");
        requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onGranted(List<String> grantedPermission) {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });
    }





}

