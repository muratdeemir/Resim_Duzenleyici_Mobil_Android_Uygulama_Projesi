package com.ss.resimduzenleyici.imageprocessing;

import android.graphics.Bitmap;

public class RomanticEmbossTransformation extends AbstractEffectTransformation {
    private ConvolutionMatrix embossMatrix;


    public RomanticEmbossTransformation() {
        int[][] config = new int[][]{
                {-1, 0, -1},
                { 0, 4,  0},
                {-1, 0, -1}
        };
        embossMatrix = new ConvolutionMatrix(config, 1, 127);
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
