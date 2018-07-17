package com.cnr.basemodule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class TokenUtils {

    /**
     * 保存服务器登录成功返回token
     *
     * @param context
     * @return
     */
    public static boolean setToken(Context context, String token) {

        SharedPreferences loginSp = context.getSharedPreferences("loMSDateUtilsginSp",
                Context.MODE_PRIVATE);
        return loginSp.edit().putString("tokenID", token).commit();
    }

    public static boolean setLoginType(Context context, String token) {

        SharedPreferences loginSp = context.getSharedPreferences("LoginType",
                Context.MODE_PRIVATE);
        return loginSp.edit().putString("login", token).commit();
    }

    public static String getLoginType(Context context) {
        SharedPreferences loginSp = context.getSharedPreferences("LoginType",
                Context.MODE_PRIVATE);
        return loginSp.getString("login", "");
    }
    /**
     * 获取服务器登录成功返回token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences loginSp = context.getSharedPreferences("loginSp",
                Context.MODE_PRIVATE);
        return loginSp.getString("tokenID", "");
    }

    /**
     * 移除服务器登录成功返回token
     *
     * @param context
     * @return
     */
    public static void removeToken(Context context) {
        SharedPreferences loginSp = context.getSharedPreferences("loginSp",
                Context.MODE_PRIVATE);
        loginSp.edit().remove("token").apply();
    }



    /**
     * Token计算方式：md5($userId.date('Y-m',time()).'#!$@%@$!#'.$sid);
     * md5用户ID+当前时间格式化的年-月+#!$@%@$!#+项目ID)
     * 例如：MD5(12017-01#!$@%@$!#1)
     * 用户接口和订购接口需要使用，用于二次判断，以防接口被破解，用户和订购接口不传此参数将不通过
     *
     * @return
     */
    public static String getToken(int Id) {

        long curr = System.currentTimeMillis();
        String time = MSDateUtils.getYearAndMonth(curr);

        int userId = Id;
        String md5String = userId + time + "#!$@%@$!#1";
        return WxMd5.MD5Encode(md5String, "utf-8");
    }


    /**
     * 判断用户是否登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getToken(context));
    }
}
