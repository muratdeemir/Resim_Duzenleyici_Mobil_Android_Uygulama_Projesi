package com.ss.resimduzenleyici.imageprocessing;

import android.graphics.Bitmap;

public interface ITransformation {
	Bitmap perform(Bitmap inp);
}
