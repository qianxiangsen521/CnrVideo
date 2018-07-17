package com.cnr.basemodule.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by hywel on 2017/12/25.
 */

public class ToastUtil {

    public static void show(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, @StringRes int strId) {
        Toast.makeText(context, context.getText(strId), Toast.LENGTH_SHORT).show();
    }

}
