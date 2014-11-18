package com.soma.tleaf.android_sns_crawler.core;

import com.facebook.Session;
import com.soma.tleaf.android_sns_crawler.network.MqttConnector;
import com.soma.tleaf.android_sns_crawler.network.tleaf.TLeafSession;

/**
 * Created by jangyoungjin on 11/6/14.
 */
public class AppContext {
    public static final String TAG = AppContext.class.getSimpleName();

    /**
     * Redirect to UI Thread
     *
     * @param r
     */
    public static void post(Runnable r) {
        CustomApplication.mInstance.mHandler.post(r);
    }

    /**
     * UI Conversion ( DP to PX )
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) (dp * CustomApplication.mInstance.mDensity);
    }

    /**
     * UI Conversion ( DP to PX )
     *
     * @param px
     * @return
     */
    public static float px2dp(int px) {
        return px / CustomApplication.mInstance.mDensity;
    }


    /**
     * Set Facebook session
     *
     * @param session
     */
    public static void setFbSession(Session session) {
        CustomApplication.mInstance.setFacebookSession(session);
    }

    /**
     * Get Facebook session
     */
    public static Session getFbSession() {
        return CustomApplication.mInstance.getFacebookSession();
    }

    /**
     * Set TLeaf session
     *
     * @param session
     */
    public static void setTlSession(TLeafSession session) {
        CustomApplication.mInstance.setTLeafSession(session);
    }

    /**
     * Get TLeaf session
     */
    public static TLeafSession getTlSession() {
        return CustomApplication.mInstance.getTLeafSession();
    }


    /**
     * GET Mqtt Connector
     * @return
     */
    public static MqttConnector getMqttConnector(){
        return CustomApplication.mInstance.getmMqttConnector();
    }

    /**
     * GET Device ID
     * @return
     */
    public static String getDeviceId(){
        return CustomApplication.mInstance.mDeviceId;
    }

    /**
     * GET Preference
     */
    public static Preference getPreference() {
        return CustomApplication.mInstance.getmPreference();
    }
}
