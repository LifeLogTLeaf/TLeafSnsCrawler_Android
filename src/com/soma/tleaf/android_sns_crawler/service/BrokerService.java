package com.soma.tleaf.android_sns_crawler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.soma.tleaf.android_sns_crawler.core.AppContext;
import com.soma.tleaf.android_sns_crawler.network.Reciever;

/**
 * Created by jangyoungjin on 11/9/14.
 */
public class BrokerService extends Service implements Reciever {
    private final static String TAG = BrokerService.class.getSimpleName();
    private final static String TOPIC = "user/call";
    private final static String CALLBACK_TOPIC = "server/callback";
    private NetworkChangeReceiver networkReceiver;


    //For test ( See data from ui )
    Handler handler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "서비스가 생성되었습니다. ");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "서비스 온크리에이트 ");
        init();
        initConnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "서비스가 죽었습니다. ");
        free();
        freeConnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRecieve(String topic, String data) {
        Log.i(TAG, "topic :  " + topic);
        //showToast(data);
        switch (topic){
            case TOPIC:
                sendAccessKey();
                break;
            default:
                break;
        }
    }

    private void init(){
        networkReceiver = new NetworkChangeReceiver();
        this.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // For test
        if(handler == null){
            handler = new Handler();
        }
    }

    private void free(){
        this.unregisterReceiver(networkReceiver);
    }

    private void initConnect() {
        AppContext.getMqttConnector().connect();
        AppContext.getMqttConnector().registerReciever(this);
    }

    private void freeConnect() {
        AppContext.getMqttConnector().unRegisterReciever(this);
        AppContext.getMqttConnector().disconnect();
    }

    private void sendAccessKey(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                String jsonString = gson.toJson(AppContext.getTlSession());
                AppContext.getMqttConnector().publish(jsonString , CALLBACK_TOPIC);
            }
        });
        t.start();
    }

    private void showToast(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *   네크워크 연결 상태를 체크하는 브로드캐스트 리시버 클래스
     */
    private class NetworkChangeReceiver extends BroadcastReceiver {
        private int TYPE_WIFI = 1;
        private int TYPE_MOBILE = 2;
        private int TYPE_NOT_CONNECTED = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (checknetworkStatus(context) != 0) {
                Log.i(TAG, "네트워크 연결됨");
                if(!AppContext.getPreference().getBooleanPref("isNetwork")){
                    AppContext.getMqttConnector().connect();
                }
                AppContext.getPreference().setBooleanPref("isNetwork", true);
            } else {
                Log.i(TAG, "네트워크 연결 끊김");
                AppContext.getPreference().setBooleanPref("isNetwork", false);
            }
        }

        private int checknetworkStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }
    }

}


















