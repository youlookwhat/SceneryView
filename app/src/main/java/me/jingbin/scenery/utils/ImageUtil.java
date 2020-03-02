package me.jingbin.scenery.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author jingbin
 * @data 2020-01-30
 */
public class ImageUtil {

    /**
     * 生成单个view的bitmap，
     */
    public static Bitmap createViewBitmap(View v) {
        if (v == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 周边边透明
        canvas.drawColor(Color.TRANSPARENT);
        v.draw(canvas);
        return bitmap;
    }


    /**
     * 处理的是gone掉的view，没有黑边！
     * 还有一种 是view完全没有显示在界面上，通过inflate 转化的view，这时候通过 DrawingCache 是获取不到bitmap 的，也拿不到view 的宽高，以上两种方法都是不可行的。
     * 第三种方法通过measure、layout 去获得view 的实际尺寸。
     * View view = LayoutInflater.from(this).inflate(R.layout.view_inflate, null, false);
     */
    public static Bitmap createViewBitmap(View v, int width, int height) {
        //测量使得view指定大小
//        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
//        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
//        v.measure(measuredWidth, measuredHeight);
//        //调用layout方法布局后，可以得到view的尺寸大小
//        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
//        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmp);
//        // 周边边透明
//        c.drawColor(Color.TRANSPARENT);
//        v.draw(c);
//        return bmp;
        if (v == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 周边边透明
        canvas.drawColor(Color.TRANSPARENT);
        v.draw(canvas);
        return bitmap;

    }


    /**
     * 注意开权限
     */
    public static void saveToLocal(Context context, Bitmap bitmap) {
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "ipicture相册");
            // 没有目录创建目录
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, "view_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                    out.flush();
                    out.close();
                    // 通知图库更新
                    Uri uri = Uri.fromFile(file);
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    context.sendBroadcast(scannerIntent);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
