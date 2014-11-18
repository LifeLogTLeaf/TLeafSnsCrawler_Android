package com.soma.tleaf.android_sns_crawler.network.tleaf;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.*;

/**
 * Created by jangyoungjin on 10/29/14.
 */
public class RequestAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = RequestAsyncTask.class.getSimpleName();
    private static final String URL = "http://14.63.171.66:8081/tleafstructure";
    private static final String USERID_HEADER_NAME = "x-tleaf-user-id";
    private static final String APPID_HEADER_NAME = "x-tleaf-application-id"; // Same as other company's API Key
    private static final String ACCESSKEY_HEADER_NAME = "x-tleaf-access-token";

    private RequestParam param;

    public RequestAsyncTask(RequestParam param) {
        this.param = param;
    }

    @Override
    protected String doInBackground(Void... params) {
        switch (param.getHttpMethod()) {
            case GET:
                getData();
                break;
            case POST:
                postData();
                break;
            default:
                break;
        }
        return null;
    }

    @Deprecated
    private void postData() {
        // Test only Post
        HttpClient client = initHttpClient();
        HttpPost httpPost = new HttpPost(URL + param.getUrl());
        Log.i(TAG, "Http Post : " + httpPost.getURI());
        httpPost.setEntity(mapJson(param.getData()));
        setHeaderParam(httpPost);
        try {
            // Execute
            StringBuilder builder = new StringBuilder();
            /**
             * 여기서 HttpStatus.SEE_OTHER 로 에러를 받으면 예외처리 된다.
             * Received redirect response HTTP/1.1 303 See Other but no location header
             */
            HttpResponse response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            int resultCode = statusLine.getStatusCode();
            HttpEntity resultEntity = response.getEntity();
            InputStream result = resultEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            param.getCallback().onRecieve(getResponse(builder.toString(), resultCode));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getData() {
        HttpClient client = initHttpClient();
        HttpGet httpGet = new HttpGet(URL + param.getUrl());
        Log.i(TAG, "Http Get : " + httpGet.getURI());
        setHeaderParam(httpGet);
        try {
            // Execute
            StringBuilder builder = new StringBuilder();
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int resultCode = statusLine.getStatusCode();
            HttpEntity resultEntity = response.getEntity();
            InputStream result = resultEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            param.getCallback().onRecieve(getResponse(builder.toString(), resultCode));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StringEntity mapJson(Object obj) {
        Gson gson = new Gson();
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(gson.toJson(obj), "UTF-8");
            stringEntity.setContentType("application/json; charset=utf-8");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    private HttpClient initHttpClient() {
        // Test only Post
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        return client;
    }

    private void setHeaderParam(HttpRequestBase httpRequest) {
        if(param.getSession() != null) {
            httpRequest.setHeader(ACCESSKEY_HEADER_NAME, param.getSession().getAccessKey());
            httpRequest.setHeader(USERID_HEADER_NAME, param.getSession().getUserId());
            httpRequest.setHeader(APPID_HEADER_NAME, param.getSession().getAppId());
        }
    }

    private Response getResponse(String jsonString, int status){
        Response response = new Response();
        response.setJsonStringData(jsonString);
        response.setStatus(status);
        return response;
    }
}
