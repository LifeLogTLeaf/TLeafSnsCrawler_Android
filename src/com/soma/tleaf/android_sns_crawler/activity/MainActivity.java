package com.soma.tleaf.android_sns_crawler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.soma.tleaf.android_sns_crawler.R;
import com.soma.tleaf.android_sns_crawler.core.AppContext;
import com.soma.tleaf.android_sns_crawler.core.DeclareView;
import com.soma.tleaf.android_sns_crawler.core.ViewMapper;
import com.soma.tleaf.android_sns_crawler.service.BrokerService;

/**
 * Created by jangyoungjin on 11/10/14.
 */
public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @DeclareView( id = R.id.main_img )
    ImageView main_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.main));

        // START SERVICE
        startService(new Intent(this, BrokerService.class));
        uiInitialize();
    }


    private void uiInitialize(){
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        if(AppContext.getPreference().getBooleanPref("isFullLogin")) main_img.startAnimation(animation);
    }
}
