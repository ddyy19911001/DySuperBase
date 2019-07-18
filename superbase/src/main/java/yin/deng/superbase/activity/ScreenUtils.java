package yin.deng.superbase.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 屏幕获取相关的辅助类
 */
public class ScreenUtils {
    public static int softKeyBoardHeight=0;
    private static final String TAG = "ScreenUtils";

    private ScreenUtils() {
        /*不能实例*/
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getApplicationContext().getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * dp --> px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dipTopx(Context context, float dipValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * px --> dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxTodip(Context context, float pxValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new
                DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight
                = dm.heightPixels;
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }

    /**
     * 获取NavigationBar的高度
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getApplicationContext().getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getApplicationContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }

        return hasNavigationBar;

    }

    /**
     * 显示软键盘
     *
     * @param etMoneyCount
     */
    public static void showSoft(Context context, EditText etMoneyCount) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etMoneyCount, InputMethodManager.SHOW_FORCED);
    }


    public static void showSoftForced(final EditText editText) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask()

                       {

                           public void run()

                           {

                               InputMethodManager inputManager =

                                       (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                               inputManager.showSoftInput(editText, 0);

                           }

                       },

                200);
    }

    public static void hideSoft(Context context, EditText etMoneyCount) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etMoneyCount.getWindowToken(), 0); //强制隐藏键盘
    }


    public static int  getSoftKeyBoardHeight(Activity activity){
        if(softKeyBoardHeight==0) {
            //判断窗口可见区域大小
            Rect r = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int screenHeight = ScreenUtils.getScreenHeight(activity);
            //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
            int heightDifference = screenHeight - (r.bottom - r.top);
            boolean isKeyboardShowing = heightDifference > screenHeight / 3;
            if (isKeyboardShowing) {
                softKeyBoardHeight = heightDifference;
                return heightDifference;
            } else {
                return 0;
            }
        }else{
            return softKeyBoardHeight;
        }
    }


    /**
     * 获取标题栏的高度
     * @param activity
     * @return
     */
    public static int getBiaoTiHeight(Activity activity){
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - getStatusHeight(activity);
        return titleBarHeight;
    }
    /**
     * 获取标题栏和状态栏总的高度
     * @param activity
     * @return
     */
    public static int getTopAllHeight(Activity activity){
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //statusBarHeight是上面所求的状态栏的高度
        return contentTop;
    }

}
