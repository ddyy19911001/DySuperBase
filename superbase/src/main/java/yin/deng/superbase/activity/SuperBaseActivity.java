package yin.deng.superbase.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.jessyan.autosize.internal.CustomAdapt;
import yin.deng.superbase.R;
import yin.deng.superbase.activity.permission.PermissionListener;
import yin.deng.superbase.activity.permission.PermissionPageUtils;


/**
 * Created by Administrator on 2018/4/12.
 * deng yin
 */
public abstract class SuperBaseActivity extends AppCompatActivity implements CustomAdapt, NetStateReceiver.OnNetStateChangeListener{
    public Dialog loadingDialog;
    public boolean isMainActivity;
    public NetStateReceiver netChangeReceiver;
    public ToastUtil toast;
    public PermissionListener permissionListener;
    public static final int permissionsRequestCode=2103;

    //1080的
    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 524;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        onNotcreate();
        super.onCreate(savedInstanceState);
        onNotSetContentView();
        setContentView(setLayout());
        onNotAddActivity();
        //将所有activity列入activity管理器中方便退出时清理
        AppActivityListManager.getScreenManager().addActivity(this);
        onNotBindView();
        bindViewWithId();
        onNotSetStatusBar();
        //去除小米手机白色状态栏
        StatuBarUtils.setStatusBarTranslucent(this,true);
        onNotSetNetWorkListener();
        initNetWork();
        //设置状态栏
        setStatusStyle(Color.WHITE);
        isMainActivity = setIsExitActivity();
        onNotInitFirst();
        initFirst();
        onInitFirstOver();
    }

    /**
     * 其他逻辑全部走完
     */
    public void onInitFirstOver() {

    }

    /**
     * 其他逻辑还没有开始
     */
    public void onNotInitFirst() {

    }

    /**
     * 还没有设置网络状态监听
     */
    public void onNotSetNetWorkListener() {

    }

    /**
     * 还没有设置状态栏
     */
    public void onNotSetStatusBar() {

    }

    /**
     * 还没有开始添加activity到管理器里面
     */
    public void onNotAddActivity() {

    }

    /**
     * 还没有绑定ViewId
     */
    public void onNotBindView() {

    }

    /**
     * 还没有开始设置布局
     */
    public void onNotSetContentView(){

    }

    /**
     * 还没有开始Create
     */
    public void onNotcreate() {

    }

    /**
     * 绑定ViewId
     */
    public void bindViewWithId() {

    }


    /**
     * 权限申请
     * @param permissions
     *            待申请的权限集合
     * @param listener
     *            申请结果监听事件
     */
    public void requestRunTimePermission(String[] permissions,
                                            PermissionListener listener) {
        permissionListener=listener;
        PackageManager pkm = getPackageManager();
        // 用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        if(permissions==null){
            LogUtils.e("权限列表为空");
            return;
        }
        for (String permission : permissions) {
            // 判断是否已经授权，未授权，则加入待授权的权限集合中
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }else {
                if (pkm.checkPermission(permission, getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
        }

        // 判断集合
        if (!permissionList.isEmpty()) { // 如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]),
                    permissionsRequestCode);
        } else { // 为空，则已经全部授权
            if(permissionListener!=null) {
                permissionListener.onGranted();
            }
        }
    }


    /**
     * 权限申请结果
     * @param requestCode
     *            请求码
     * @param permissions
     *            所有的权限集合
     * @param grantResults
     *            授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case permissionsRequestCode:
                if (grantResults.length > 0) {
                    // 被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    // 用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        // 获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { // 用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else { // 用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) { // 用户拒绝权限为空
                        if(permissionListener!=null){
                            permissionListener.onGranted();
                        }
                    } else { // 不为空
                        // 回调授权成功的接口
                        if(permissionListener!=null){
                            //用户拒绝的部分权限
                            permissionListener.onDenied(deniedPermissions);
                            //用户同意的部分权限
                            permissionListener.onGranted(grantedPermissions);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }



    /**
     * 设置状态栏背景及字体颜色
     */
    public void setStatusStyle(int statusColorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(statusColorRes);
            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    public void initNetWork() {
        IntentFilter intentFilter = new IntentFilter();
        //当前网络发生变化后，系统会发出一条值为android.net.conn.CONNECTIVITY_CHANGE的广播，所以要监听它
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetStateReceiver();
        netChangeReceiver.setOnNetStateChangeListener(this);
        //进行注册
        registerReceiver(netChangeReceiver, intentFilter);
    }

    /**
     * 处理断网
     */
    @Override
    public void onNetChange(int state) {

    }





    public void showTs(String msg) {
        if (toast == null) {
            toast = new ToastUtil(this, msg);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public String getPhoneType() {
        return Build.BRAND + "-" + Build.MODEL;
    }


    public boolean setIsExitActivity() {
        return false;
    }

    /**
     * 重写onBackPressed监听返回，统一设置了返回动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isMainActivity) {
            overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netChangeReceiver);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }


    @Override
    public void onStart() {
        super.onStart();
    }



    public abstract int setLayout();

    public abstract void initFirst();



    /**
     * 重写startActivity，统一设置启动Activity的动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.new_to_left, R.anim.old_to_left);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isMainActivity) {
                        appExit();
                        return false;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void appExit() {
        AppExit2Back.exitApp(this);
    }


    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionNum() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }


    /**
     * 获取应用名称
     *
     * @return
     */
    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }


    /**
     * 获取唯一设备Id
     *
     * @return
     */
    public String getPhoneId() {
        //权限校验
        return getUniquePsuedoID();
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 显示转圈的Dialog
     *
     * @return
     */
    public Dialog showLoadingDialog(String msg, boolean canDismiss) {
        closeDialog();
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(this, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(canDismiss); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        this.loadingDialog = loadingDialog;
        return loadingDialog;
    }


    /**
     * 关闭dialog
     */
    public void closeDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            this.loadingDialog.dismiss();
            this.loadingDialog = null;
        }
    }

}
