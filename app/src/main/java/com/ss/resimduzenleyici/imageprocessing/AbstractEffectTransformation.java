package com.ss.resimduzenleyici.imageprocessing;

import android.graphics.Bitmap;


public abstract class AbstractEffectTransformation extends AbstractBaseTransformation {
    Bitmap thumbImage;

    public abstract void setThumbImage(Bitmap bmThumb);
    public abstract Bitmap getThumbnail();

}
