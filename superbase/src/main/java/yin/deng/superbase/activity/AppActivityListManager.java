package yin.deng.superbase.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/**
 * 主要功能: 管理和回收Act
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017年05月03日 16:37
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */
public class AppActivityListManager {

    
    //存储ActivityStack
    private static Stack<Activity> activityStack = new Stack<Activity>();

    //单例模式
    private static AppActivityListManager instance;


    /**
     * 单列堆栈集合对象
     * @return AppActivityListManager 单利堆栈集合对象
     */
    public static AppActivityListManager getScreenManager() {
        if (instance == null) {
            synchronized (AppActivityListManager.class){
                if (instance == null) {
                    instance = new AppActivityListManager();
                }
            }
        }
        return instance;
    }
    

    /**
     * 堆栈中销毁并移除
     * @param activity 指定Act对象
     */
    public void removeActivity(Activity activity) {
        LogUtils.i("AppActivityListManager-->>removeActivity"+ activity != null ? activity.toString() : "");
        if (null != activity) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }



    /**
     * 栈中销毁并移除所有Act对象
     */
    public void removeAllActivity() {
        if (null != activityStack && activityStack.size() > 0) {
                //创建临时集合对象
                Stack<Activity> activityTemp = new Stack<Activity>();
                for (Activity activity : activityStack) {
                    if (null != activity) {
                        //添加到临时集合中
                        activityTemp.add(activity);
                        //结束Activity
                        activity.finish();
                    }
                }
                activityStack.removeAll(activityTemp);
        }
        LogUtils.i("AppActivityListManager-->>removeAllActivity"+ "removeAllActivity");
        System.gc();
        System.exit(0);
    }


    /**
     * 获取当前Act对象
     * @return Activity 当前act
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty()){
            activity = activityStack.lastElement();
        }
        LogUtils.i("AppActivityListManager-->>currentActivity"+ activity + "");
        return activity;
    }


    /**
     * 获得当前Act的类名
     * @return String
     */
    public String getCurrentActivityName() {
        String actSimpleName = "";
        if (!activityStack.empty()) {
            actSimpleName = activityStack.lastElement().getClass().getSimpleName();
        }
        LogUtils.i("AppActivityListManager-->>getCurrentActivityName"+ actSimpleName);
        return actSimpleName;
    }


    /**
     * 将Act纳入推栈集合中
     * @param activity Act对象
     */
    public void addActivity(Activity activity) {
        LogUtils.i("AppActivityListManager-->>addActivity"+  activity != null ? activity.toString() : "");
        if (null == activityStack) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


    public void exitApp(Context context){
        AppActivityListManager.getScreenManager().removeAllActivity();
        //创建ACTION_MAIN
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Context content = ((Activity) context);
        //启动ACTION_MAIN
        content.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
