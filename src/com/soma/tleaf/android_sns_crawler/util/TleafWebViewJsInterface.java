/**
 * 
 */
package com.soma.tleaf.android_sns_crawler.util;

import com.google.gson.Gson;
import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.soma.tleaf.android_sns_crawler.core.AppContext;
import com.soma.tleaf.android_sns_crawler.network.tleaf.TLeafSession;

/**
 * Created with Eclipse IDE
 * Author : RichardJ 
 * Date   : Nov 17, 2014 3:30:39 PM
 * Description : 
 */
public class TleafWebViewJsInterface {
	private static final String TAG = "JAVASCIRPT INTERFACE";
    Activity mActivity;

    public TleafWebViewJsInterface(Activity activity){
        mActivity = activity;
    }

    @JavascriptInterface
    public void callbackAndroid(final String str){
        Log.i(TAG, str);
        TLeafSession session = new Gson().fromJson(str, TLeafSession.class);
        AppContext.setTlSession(session);
        AppContext.getPreference().setBooleanPref("isLogin", true);
        mActivity.finish();
    }
}
