package com.soma.tleaf.android_sns_crawler.network.tleaf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jangyoungjin on 10/29/14.
 */
public class RequestQueryParam {
    private Map<String, Object> appData;

    public RequestQueryParam(){
        appData = new HashMap<String, Object>();
    }

    public void putData(String key, Object value){
        appData.put(key, value);
    }

    public void getData(String key){
        appData.get(key);
    }

    public void contain(Object value){
        appData.containsValue(value);
    }

}
