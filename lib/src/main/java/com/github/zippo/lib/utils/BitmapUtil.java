package com.github.zippo.lib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Zippo on 2018/2/23.
 * Date: 2018/2/23
 * Time: 15:45:50
 */

public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    /**
     * 质量压缩：根据传递进去的质量大小，采用系统自带的压缩算法，将图片压缩成JPEG格式
     *
     * @param bitmap  bitmap
     * @param quality 图片的质量
     * @param file    输出文件所在的路径
     */
    public static void compressQuality(Bitmap bitmap, int quality, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            LeLog.w(TAG, e);
        }
    }

    /**
     * 尺寸压缩：根据图片的缩放比例进行等比大小的缩小尺寸，从而达到压缩的效果
     *
     * @param bitmap bitmap
     * @param file   输出文件所在的路径
     */
    public static void compressSize(Bitmap bitmap, File file) {
        int ratio = 8;//尺寸压缩比例
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);
        compressQuality(result, 100, file);
    }

    /**
     * 采样率压缩：根据图片的采样率大小进行压缩
     *
     * @param filePath 输出文件所在的路径
     * @param file     输出文件
     */
    public static void compressSample(String filePath, File file) {
        int inSampleSize = 8;//采样率设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        compressQuality(bitmap, 100, file);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
