package me.jingbin.scenery.wave;

import android.content.Context;

public class DensityUtil {

//    public static int dip2px(Context var0, float var1) {
//        float var2 = var0.getResources().getDisplayMetrics().density;
//        return ( int ) (var1 * var2 + 0.5F);
//    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
