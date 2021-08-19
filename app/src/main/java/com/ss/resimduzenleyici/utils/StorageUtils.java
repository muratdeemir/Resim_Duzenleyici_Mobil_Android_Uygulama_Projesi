package com.ss.resimduzenleyici.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.ss.resimduzenleyici.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class StorageUtils {

    public static String getPathFromUri(Uri uri, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
                null, null, null);
        String filePath;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    public static File getPublicDirectiry() {
        File file = new File(Environment.getExternalStorageDirectory(),
                AppConstants.APP_FOLDER);
        if (file.mkdirs()) {
            GLog.v("Uygulama Klasörü", file.getPath() + " oluşturuldu");
        }
        return file;
    }

    public static File saveBitmap(Bitmap bm, File location) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(location);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            GLog.v("Dosya buraya kaydedildi", location.getPath());
            return location;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getNewAppsFile() {
        @SuppressLint("SimpleDateFormat") String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (isExternalStorageWritable()) {
            File folder = getPublicDirectiry();
            File file = new File(folder, AppConstants.DEFAULT_FILE_PREFIX + id
                    + ".JPEG");
            return file;
        } else {
            GLog.v("BİTMAP KAYDET", "HARİCİ DEPOLAMAYA KAYDEDİLEMİYOR");
            return null;
        }
    }

    public static void copyFile(File src, File dest) throws IOException {
        File destFolder = dest.getParentFile();
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getCacheDirectory() {
        File file = new File(Environment.getExternalStorageDirectory(),
                AppConstants.APP_CACHE_FOLDER);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                GLog.v("CACHE KLASÖRÜ", "oluşturulmadı");
            }
        }

        return file;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File createImageFile(String prefix, File dir, String extension) {
        @SuppressLint("SimpleDateFormat") String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (isExternalStorageWritable()) {
            try {
                File file = new File(dir, prefix + id + extension);
                if (!file.exists()) {
                    Objects.requireNonNull(file.getParentFile()).mkdirs();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ignored) {

            }
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public static String convertToMiB(long length) {
        int kb = (int) Math.ceil(length / 1024.0f);
        if (kb >= 1024) {
            float mib = length / (1024 * 1024.0f);
            return String.format("%2f", mib) + " MB";
        }
        return String.format("%4d", kb) + " KB";
    }
}
