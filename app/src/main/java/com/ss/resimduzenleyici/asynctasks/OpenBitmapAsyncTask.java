package com.ss.resimduzenleyici.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ss.resimduzenleyici.AppConfigs;
import com.ss.resimduzenleyici.BuildConfig;
import com.ss.resimduzenleyici.utils.BitmapUtils;
import com.ss.resimduzenleyici.utils.GLog;

import java.lang.ref.WeakReference;

public class OpenBitmapAsyncTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<ImageView> imgvRef;
    private WeakReference<ProgressDialog> progress;
    private Context context;
    private String path;
    private Bitmap image;
    private int reqW;
    private int reqH;
    private boolean isSetSizeManual;
    private OnBitmapOpenedListener openedListener;


    private OpenBitmapAsyncTask(Builder builder) {
        this.context = builder.context;
        if (builder.imgv != null) {
            this.imgvRef = new WeakReference<ImageView>(builder.imgv);
        }
        if (builder.progress != null) {
            this.progress = new WeakReference<ProgressDialog>(builder.progress);
        }
        this.path = builder.path;
        this.isSetSizeManual = builder.isSetSizeManual;
        this.reqW = builder.reqW;
        this.reqH = builder.reqH;
        this.openedListener = builder.openedListener;
    }


    @Override
    protected void onPreExecute() {
        if (!isSetSizeManual) {
            reqH = AppConfigs.getInstance().deviceWidth;
            reqW = AppConfigs.getInstance().deviceHeight;
        }
        if (progress != null) {
            final ProgressDialog prg = progress.get();
            if (prg != null) {
                prg.show();
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = BitmapUtils.calculateSampleSize(options, reqW, reqH);
        options.inJustDecodeBounds = false;
        image = BitmapFactory.decodeFile(path, options);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        if (imgvRef != null && image != null) {
            final ImageView imageView = imgvRef.get();
            if (imageView != null) {
                Drawable drawable = imageView.getDrawable();
                if (drawable != null && drawable instanceof BitmapDrawable) {
                    BitmapDrawable bm = (BitmapDrawable) drawable;
                    bm.getBitmap().recycle();
                }
                imageView.setImageBitmap(image);
                imageView.invalidate();
                if (BuildConfig.DEBUG) {
                    GLog.v("Async Yükleniyor", "W=" + image.getWidth() + "  H="
                            + image.getHeight());
                }
            }
        }
        if (this.openedListener != null) {
            this.openedListener.onBitmapOpened(image);
        }
        if (progress != null) {
            final ProgressDialog prg = progress.get();
            if (prg != null) {
                prg.dismiss();
            }
        }
    }


    public interface OnBitmapOpenedListener {
        void onBitmapOpened(Bitmap bm);
    }

    public static class Builder {
        private ImageView imgv;
        private ProgressDialog progress;
        private Context context;
        private String path;
        private int reqW;
        private int reqH;
        private boolean isSetSizeManual;
        private OnBitmapOpenedListener openedListener;

        public OpenBitmapAsyncTask build() {
            return new OpenBitmapAsyncTask(this);
        }

        public Builder(Context context, String path) {
            this.context = context;
            this.path = path;
        }

        public Builder setRequestSize(int width, int height) {
            this.isSetSizeManual = true;
            this.reqW = width;
            this.reqH = height;
            return this;
        }

        public Builder setProgress(ProgressDialog progress) {
            this.progress = progress;
            return this;
        }

        public Builder setImageView(ImageView imgv) {
            this.imgv = imgv;
            return this;
        }

        public Builder setBitmapOpenedListener(OnBitmapOpenedListener listener) {
            this.openedListener = listener;
            return this;
        }
    }

}