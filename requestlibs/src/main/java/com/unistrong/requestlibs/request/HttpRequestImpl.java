package com.unistrong.requestlibs.request;

import android.os.Handler;

import com.unistrong.requestlibs.inter.IRequest;
import com.unistrong.requestlibs.response.ResponseBody;

import java.util.HashMap;

public class HttpRequestImpl extends BaseRequest implements IRequest {

    public static HttpRequestImpl instance;
    private static Handler handler = new Handler();

    public static HttpRequestImpl getInstance() {
        return instance == null ? new HttpRequestImpl() : instance;
    }

    @Override
    public void request(String action, int method, Object params, int timeout, long tag, ResponseBody listener) {
        request_(action, method, params, timeout, tag, handler, listener);
    }

    @Override
    public void requestGet(String action, HashMap<String, String> params, ResponseBody listener) {
        request(action, Method.GET, params, DEF_TIMEOUT, System.currentTimeMillis(), listener);
    }

    @Override
    public void requestPost(String action, HashMap<String, String> params, ResponseBody listener) {
        request(action, Method.POST_AS_JSON, params, DEF_TIMEOUT, System.currentTimeMillis(), listener);
    }

    @Override
    public void requestPostForm(String action, HashMap<String, String> params, ResponseBody listener) {
        request(action, Method.POST_AS_FORM, params, DEF_TIMEOUT, System.currentTimeMillis(), listener);
    }


    @Override
    public void stopRequest() {
        deleteCall();
    }

    @Override
    public void stopRequest(long tag) {
        deleteCall();
    }
}
