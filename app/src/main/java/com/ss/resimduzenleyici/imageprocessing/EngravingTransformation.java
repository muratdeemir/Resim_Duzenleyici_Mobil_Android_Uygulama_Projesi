package com.ss.resimduzenleyici.imageprocessing;

import android.graphics.Bitmap;

public class EngravingTransformation extends AbstractEffectTransformation {

    private ConvolutionMatrix engravingMatrix;

    public EngravingTransformation() {
        int[][] config = new int[][]{
                {-2, 0, 0},
                {0, 2, 0},
                {0, 0, 0}
        };

        engravingMatrix = new ConvolutionMatrix(config, 1, 95);
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
        return engravingMatrix.convolute(inp);
    }
}
