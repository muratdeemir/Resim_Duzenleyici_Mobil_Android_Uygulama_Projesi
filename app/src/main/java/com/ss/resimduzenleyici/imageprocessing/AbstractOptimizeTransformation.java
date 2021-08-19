package com.ss.resimduzenleyici.imageprocessing;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.widget.SeekBar;

import com.ss.resimduzenleyici.asynctasks.DoTransformationAsyncTask;

public abstract class AbstractOptimizeTransformation extends AbstractBaseTransformation {

    protected Bitmap image;
    private SeekBar seekBar;
    int maxValue;
    protected int minValue;
    int currentValue;
    ProgressDialog progressDialog;
    DoTransformationAsyncTask transTask;
    DoTransformationAsyncTask.OnPerformedListener onPerformedListener;

    public void setupTransformation(SeekBar seekBar, Bitmap bm, DoTransformationAsyncTask transformationAsyncTask, DoTransformationAsyncTask.OnPerformedListener onPerformedListener) {
        this.seekBar = seekBar;
        this.onPerformedListener = onPerformedListener;
        this.image = bm;
        this.transTask = transformationAsyncTask;
        setupSeekBar(this.seekBar);
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    protected abstract void setupSeekBar(SeekBar seekBar);

}
