package com.soma.tleaf.android_sns_crawler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
public class TleafSignUpActivity extends Activity implements View.OnClickListener, Request.Callback {
    private static final String TAG = TleafSignUpActivity.class.getSimpleName();

    @DeclareView(id = R.id.tleafsignup_btn_signup, click = "this")
    TextView tleafsignup_btn_signup;

    @DeclareView(id = R.id.tleafsignup_btn_cancel, click = "this")
    TextView tleafsignup_btn_cancel;

    @DeclareView(id = R.id.tleafsignup_etx_id)
    EditText tleafsignup_etx_id;

    @DeclareView(id = R.id.tleafsignup_etx_password)
    EditText tleafsignup_etx_password;

    @DeclareView(id = R.id.tleafsignup_etx_password_check)
    EditText tleafsignup_etx_password_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.tleaf_signup));
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
            case R.id.tleafsignup_btn_signup:
                if(checkInputFild()){
                    perfomLogin();
                }
                break;
            case R.id.tleafsignup_btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean checkInputFild(){
        if(tleafsignup_etx_id.getText().toString().isEmpty() || tleafsignup_etx_password.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    private void perfomLogin(){
        new Request("/user/signup", HttpMethod.POST, this, new UserInfo(tleafsignup_etx_id.getText().toString(), tleafsignup_etx_password.getText().toString())).execute();
    }

    @Override
    public void onRecieve(Response response) {
        Log.i(TAG, response.getJsonStringData());
        if(response.getStatus() == 200 ){
            finish();
        }
    }
}
