package com.cnr.cnrvideo.fragment;

import com.cnr.cnrvideo.fragment.response.VideoListResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;

public interface Webservice {

    public static boolean IS_RELEASE = false;
    public static String BASE_URL = IS_RELEASE ? "http://api.tea.cnrmobile.com/" : "http://api.tea.test.cnrmobile.com/";


    @POST("tearoad/vedioList")
    Observable<VideoListResponse> getVideoListResponse(@FieldMap Map<String,String> map);
}
