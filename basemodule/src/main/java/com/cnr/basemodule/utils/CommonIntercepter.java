package com.cnr.basemodule.utils;



import com.cnr.basemodule.base.BaseApplication;
import com.cnr.basemodule.utils.SystemUtils;
import com.cnr.basemodule.utils.TokenUtils;

import java.io.IOException;


import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OKHttp拦截器用来添加公共参数
 */

public class CommonIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        //添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());

        Request newRequest;
        Request.Builder newBuilder = oldRequest.newBuilder();
        newBuilder.url(authorizedUrlBuilder.build());
        newBuilder.addHeader("Content-type", "application/json");

        if (oldRequest.method().equals("GET")) {
            newRequest = newBuilder.method(oldRequest.method(), oldRequest.body()).build();
        } else if (oldRequest.method().equals("POST")) {
            newRequest = newBuilder.method(oldRequest.method(), addParmasToFormBody((FormBody) oldRequest.body())).build();
        } else {
            newRequest = newBuilder.build();
        }


        return chain.proceed(newRequest);
    }

    private RequestBody addParmasToFormBody(FormBody formBody) {

        FormBody.Builder builder = new FormBody.Builder();
        if (formBody == null) {
            return builder.build();
        }

        //公共参数
        String sn = SystemUtils.getMd5UniqueID(BaseApplication.getInstance());
        String sid = "1";
        int userId = 0;
        int userType = 0;
        String platform = "0";
        String channelId = "CNRL010001";
        String imei = SystemUtils.getIMEI(BaseApplication.getInstance());
        String token = TokenUtils.getToken(userId);

        builder.add("sn", sn);
        builder.add("platform", platform);
        builder.add("sid", sid);
        builder.add("channelId", channelId);
        builder.add("imei", imei);
        builder.add("userId", userId + "");
        builder.add("userType", userType + "");
        builder.add("token", token);
        builder.add("imsi", SystemUtils.getImsi(BaseApplication.getInstance()) + "");
        for (int i = 0; i < formBody.size(); i++) {
            builder.add(formBody.name(i), formBody.value(i));
        }
        return builder.build();
    }

}
