/**
 *
 */
package com.soma.tleaf.android_sns_crawler.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.soma.tleaf.android_sns_crawler.R;
import com.soma.tleaf.android_sns_crawler.core.ViewMapper;
import com.soma.tleaf.android_sns_crawler.util.TleafWebViewJsInterface;

/**
 * Created with Eclipse IDE
 * Author : RichardJ
 * Date   : Nov 17, 2014 3:23:40 PM
 * Description :
 */
public class TleafWebViewActivity extends Activity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.tleaf_webview));
        setWebView();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

    @SuppressLint("JavascriptInterface")
    public void setWebView() {
        mWebView = (WebView) findViewById(R.id.shacklogin_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new TleafWebViewJsInterface(this), "TLeafLogin");
        mWebView.loadUrl("http://14.63.171.66:8081/tleafstructure/oauth?appId=6bf388c44969b9b9a84242e1b201d80d");

    }
}
