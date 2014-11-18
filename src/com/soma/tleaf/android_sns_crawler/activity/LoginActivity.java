package com.soma.tleaf.android_sns_crawler.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.soma.tleaf.android_sns_crawler.R;
import com.soma.tleaf.android_sns_crawler.core.AppContext;
import com.soma.tleaf.android_sns_crawler.core.DeclareView;
import com.soma.tleaf.android_sns_crawler.core.ViewMapper;
import com.soma.tleaf.android_sns_crawler.network.tleaf.HttpMethod;
import com.soma.tleaf.android_sns_crawler.network.tleaf.Request;
import com.soma.tleaf.android_sns_crawler.network.tleaf.Response;
import com.soma.tleaf.android_sns_crawler.network.tleaf.TLeafSession;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements Session.StatusCallback, View.OnClickListener, Request.Callback {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private UiLifecycleHelper uiHelper;
    private boolean fbReady, tlReady;
    private static final String APPID = "6bf388c44969b9b9a84242e1b201d80d";

    @DeclareView(id = R.id.login_btn_fb)
    LoginButton login_btn_fb;

    @DeclareView(id = R.id.login_ll_tl, click = "this")
    LinearLayout login_ll_tl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.login));
        loadHashKey();
        facebookSetting();
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSession();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void call(Session session, SessionState state, Exception exception) {
        onFacebookSessionStateChange(session, state, exception);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_ll_tl:
                startActivity(new Intent(this, TleafWebViewActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onRecieve(Response response) {

    }

    private void onFacebookSessionStateChange(Session session, SessionState state, Exception e) {
        Log.i(TAG, "Facebook AccessKey : " + session.getAccessToken());
        Log.i(TAG, "Facebook Application ID : " + session.getApplicationId());
        Log.i(TAG, "Facebook Permissions : " + session.getPermissions());
        Log.i(TAG, "Facebook ExpirationDate: " + session.getExpirationDate());
        if (state.isOpened()) {
            AppContext.setFbSession(session);
            login_btn_fb.setBackgroundResource(R.drawable.shape_orange);
            fbReady = true;
        } else if (state.isClosed()) {
            login_btn_fb.setBackgroundResource(R.drawable.shape_grey);
            AppContext.getPreference().setBooleanPref("isFullLogin", false);
            fbReady = false;
        }
    }

    private void onTleafSessionStatusChange(TLeafSession session) {
        Log.i(TAG, "TLeaf AccessKey : " + session.getAccessKey());
        Log.i(TAG, "TLeaf Application ID : " + session.getAppId());
        Log.i(TAG, "TLeaf User ID : " + session.getUserId());
        /* 세션을 체크합니다. */
        tlReady = true;
        login_ll_tl.setBackgroundResource(R.drawable.shape_orange);
    }

    private void checkSession(){
        // FB session check
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onFacebookSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

        // TL session check
        TLeafSession tLeafSession = AppContext.getTlSession();
        if (tLeafSession != null /* && tleafSesson */) {
            onTleafSessionStatusChange(tLeafSession);
        }

        // if all session is green, main activity will start
        if (fbReady && tlReady) {
            AppContext.getPreference().setBooleanPref("isFullLogin", true);
            updateUserInfo();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    private void facebookSetting() {
        uiHelper = new UiLifecycleHelper(this, this);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("read_stream");
        login_btn_fb.setReadPermissions(permissions);
        // 페이스북 로그인 버튼을 변경한다.
        login_btn_fb.setBackgroundResource(R.drawable.shape_grey);
    }

    private void loadHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {

        }
    }

    private void updateUserInfo(){
        Map<String, Object> data = new HashMap<>();
        data.put("appId", APPID);
        data.put("accessToken", AppContext.getFbSession().getAccessToken());
        new Request(AppContext.getTlSession(), "/api/user", HttpMethod.POST, this, data).execute();
    }

}