package yin.deng.superbase.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * Created by Administrator on 2017/2/20.
 */

public class NetStateReceiver extends BroadcastReceiver {
    public static final int wifi=1;
    public static final int moblie=2;
    public static final int noNet=0;
    OnNetStateChangeListener onNetStateChangeListener;

    public void setOnNetStateChangeListener(OnNetStateChangeListener onNetStateChangeListener) {
        this.onNetStateChangeListener = onNetStateChangeListener;
    }

    public interface OnNetStateChangeListener{
        void onNetChange(int state);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if(activeInfo!=null){
            //准备登陆
            if(activeInfo.getTypeName().equalsIgnoreCase("WIFI")){
                if(onNetStateChangeListener!=null){
                    onNetStateChangeListener.onNetChange(wifi);
                }
            }else{
                if(onNetStateChangeListener!=null){
                    onNetStateChangeListener.onNetChange(moblie);
                }
            }
        }else{
            if(onNetStateChangeListener!=null){
                onNetStateChangeListener.onNetChange(noNet);
            }
        }
    }
}
