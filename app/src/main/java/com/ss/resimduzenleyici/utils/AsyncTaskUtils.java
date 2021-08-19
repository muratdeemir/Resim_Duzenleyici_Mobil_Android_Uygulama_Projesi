package com.ss.resimduzenleyici.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

public class AsyncTaskUtils {

    @SafeVarargs
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <T> void executeTask(AsyncTask<T, ?, ?> task, T... params) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
