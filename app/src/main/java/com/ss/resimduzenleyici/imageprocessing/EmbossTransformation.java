package com.ss.resimduzenleyici.imageprocessing;

import android.graphics.Bitmap;

public class EmbossTransformation extends AbstractEffectTransformation {

    private ConvolutionMatrix embossMatrix;


    public EmbossTransformation() {
        int[][] config = new int[][]{
                {-1, -1, 0},
                {-1,  0, 1},
                { 0,  1, 1}
        };

        embossMatrix = new ConvolutionMatrix(config, 1, 128);
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
        return embossMatrix.convolute(inp);
    }
}
