package com.ss.resimduzenleyici.imageprocessing;

import android.graphics.Bitmap;

public class SmoothTransformation extends AbstractEffectTransformation {

    private ConvolutionMatrix smMatrix;

    public SmoothTransformation() {
        int[][] config = new int[][]{
                {1, 1, 1},
                {1, 7, 1},
                {1, 1, 1}
        };

        smMatrix = new ConvolutionMatrix(config, 15, 1);
    }

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        return smMatrix.convolute(inp);
    }
}
