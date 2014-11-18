package com.soma.tleaf.android_sns_crawler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.soma.tleaf.android_sns_crawler.R;
import com.soma.tleaf.android_sns_crawler.core.AppContext;
import com.soma.tleaf.android_sns_crawler.core.DeclareView;
import com.soma.tleaf.android_sns_crawler.core.ViewMapper;
import com.soma.tleaf.android_sns_crawler.network.tleaf.*;

/**
 * Created by jangyoungjin on 11/12/14.
 */
public class TleafLoginActivity extends Activity implements View.OnClickListener, Request.Callback{
    private static final String TAG = TleafLoginActivity.class.getSimpleName();

    @DeclareView(id = R.id.tleaflogin_btn_login, click = "this")
    TextView tleaflogin_btn_login;

    @DeclareView(id = R.id.tleaflogin_btn_cancel, click = "this")
    TextView tleaflogin_btn_cancel;

    @DeclareView(id = R.id.tleaflogin_btn_signup, click = "this")
    TextView tleaflogin_btn_signup;

    @DeclareView(id = R.id.tleaflogin_etx_id)
    EditText tleaflogin_etx_id;

    @DeclareView(id = R.id.tleaflogin_etx_password)
    EditText tleaflogin_etx_password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.tleaf_login));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tleaflogin_btn_login:
                if(checkInputFild()){
                    perfomLogin();
                }
                break;
            case R.id.tleaflogin_btn_cancel:
                finish();
                break;
            case R.id.tleaflogin_btn_signup:
                startActivity(new Intent(this, TleafSignUpActivity.class));
                break;
            default:
                break;
        }

    }

    private boolean checkInputFild(){
        if(tleaflogin_etx_id.getText().toString().isEmpty() || tleaflogin_etx_password.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    private void perfomLogin(){
        new Request("/user/login", HttpMethod.POST, this, new UserInfo(tleaflogin_etx_id.getText().toString(), tleaflogin_etx_password.getText().toString())).execute();
    }

    @Override
    public void onRecieve(Response response) {
        Log.i(TAG, response.getJsonStringData());
        if(response.getStatus() == 200 ){
            AppContext.setTlSession(new Gson().fromJson(response.getJsonStringData(), TLeafSession.class));
            AppContext.getPreference().setBooleanPref("tleafSession", true);
            finish();
        }
    }
}
