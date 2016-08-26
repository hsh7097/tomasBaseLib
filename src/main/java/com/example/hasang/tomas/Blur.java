package com.example.hasang.tomas;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by hasang on 16. 1. 22..
 */
public class Blur {

    public void fastBlur(Context context, Bitmap sentBitmap, int radius, BlurringCompleteListener listener) {
        mListener = listener;
        new BlurAsyncTask(context, sentBitmap, radius);
    }

    private BlurringCompleteListener mListener;

    public interface BlurringCompleteListener {
        void onComplete(Bitmap bitmap);
    }

    private void notifyResult(Bitmap bitmap) {
        if (mListener == null) return;
        mListener.onComplete(bitmap);
    }

    private class BlurAsyncTask extends AsyncTask<Object, Integer, Bitmap> {
        private Context mContext;
        private Bitmap mInput;
        private int mRadius;

        public BlurAsyncTask(Context context, Bitmap sentBitmap, int radius) {
            mContext = context;
            mInput = sentBitmap;
            mRadius = radius;
        }

        @Override
        protected Bitmap doInBackground(Object... obj) {
            return run();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            notifyResult(bitmap);
        }

        private Bitmap run() {
            Bitmap bitmap;
            try {
                if (Build.VERSION.SDK_INT > 16) {
                    bitmap = mInput.copy(mInput.getConfig().ARGB_8888, true);

                    final RenderScript rs = RenderScript.create(mContext);
                    final Allocation input = Allocation.createFromBitmap(rs, mInput, Allocation.MipmapControl.MIPMAP_NONE,
                            Allocation.USAGE_SCRIPT);
                    final Allocation output = Allocation.createTyped(rs, input.getType());
                    final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                    script.setRadius(mRadius /* e.g. 3.f */);
                    script.setInput(input);
                    script.forEach(output);
                    output.copyTo(bitmap);
                    return bitmap;
                }
                bitmap = mInput.copy(mInput.getConfig(), true);

                if (mRadius < 1) {
                    return null;
                }

                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int[] pix = new int[w * h];
                bitmap.getPixels(pix, 0, w, 0, 0, w, h);

                int wm = w - 1;
                int hm = h - 1;
                int wh = w * h;
                int div = mRadius + mRadius + 1;

                int r[] = new int[wh];
                int g[] = new int[wh];
                int b[] = new int[wh];
                int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
                int vmin[] = new int[Math.max(w, h)];

                int divsum = (div + 1) >> 1;
                divsum *= divsum;
                int dv[] = new int[256 * divsum];
                for (i = 0; i < 256 * divsum; i++) {
                    dv[i] = (i / divsum);
                }

                yw = yi = 0;

                int[][] stack = new int[div][3];
                int stackPointer;
                int stackStart;
                int[] sir;
                int rbs;
                int r1 = mRadius + 1;
                int routsum, goutsum, boutsum;
                int rinsum, ginsum, binsum;

                for (y = 0; y < h; y++) {
                    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                    for (i = -mRadius; i <= mRadius; i++) {
                        p = pix[yi + Math.min(wm, Math.max(i, 0))];
                        sir = stack[i + mRadius];
                        sir[0] = (p & 0xff0000) >> 16;
                        sir[1] = (p & 0x00ff00) >> 8;
                        sir[2] = (p & 0x0000ff);
                        rbs = r1 - Math.abs(i);
                        rsum += sir[0] * rbs;
                        gsum += sir[1] * rbs;
                        bsum += sir[2] * rbs;
                        if (i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }
                    }
                    stackPointer = mRadius;

                    for (x = 0; x < w; x++) {

                        r[yi] = dv[rsum];
                        g[yi] = dv[gsum];
                        b[yi] = dv[bsum];

                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;

                        stackStart = stackPointer - mRadius + div;
                        sir = stack[stackStart % div];

                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];

                        if (y == 0) {
                            vmin[x] = Math.min(x + mRadius + 1, wm);
                        }
                        p = pix[yw + vmin[x]];

                        sir[0] = (p & 0xff0000) >> 16;
                        sir[1] = (p & 0x00ff00) >> 8;
                        sir[2] = (p & 0x0000ff);

                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];

                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;

                        stackPointer = (stackPointer + 1) % div;
                        sir = stack[(stackPointer) % div];

                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];

                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];

                        yi++;
                    }
                    yw += w;
                }
                for (x = 0; x < w; x++) {
                    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                    yp = -mRadius * w;
                    for (i = -mRadius; i <= mRadius; i++) {
                        yi = Math.max(0, yp) + x;

                        sir = stack[i + mRadius];

                        sir[0] = r[yi];
                        sir[1] = g[yi];
                        sir[2] = b[yi];

                        rbs = r1 - Math.abs(i);

                        rsum += r[yi] * rbs;
                        gsum += g[yi] * rbs;
                        bsum += b[yi] * rbs;

                        if (i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }

                        if (i < hm) {
                            yp += w;
                        }
                    }
                    yi = x;
                    stackPointer = mRadius;
                    for (y = 0; y < h; y++) {
                        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;

                        stackStart = stackPointer - mRadius + div;
                        sir = stack[stackStart % div];

                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];

                        if (x == 0) {
                            vmin[y] = Math.min(y + r1, hm) * w;
                        }
                        p = x + vmin[y];

                        sir[0] = r[p];
                        sir[1] = g[p];
                        sir[2] = b[p];

                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];

                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;

                        stackPointer = (stackPointer + 1) % div;
                        sir = stack[stackPointer];

                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];

                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];

                        yi += w;
                    }
                }
                bitmap.setPixels(pix, 0, w, 0, 0, w, h);
                return bitmap;
            } catch (Exception e) {
                return null;
            }
        }
    }

}
