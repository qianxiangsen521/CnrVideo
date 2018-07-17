package com.cnr.cnrvideo.fragment;

import com.cnr.cnrvideo.response.PlayInfoResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Webservice {

    @POST("program/playinfo")
    @FormUrlEncoded
    Observable<PlayInfoResponse> getPlayInfoResponse(@FieldMap Map<String, String> params);


}
