package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hero {

    // Hero variables
    private Bitmap spritesheet;

    private int score;

    // y value very time we touch the screen
    private double dya;

    // is hero going up or down
    private boolean up;

    // is the game started?
    private boolean playing;

    // we need reference to the bitmap. with and height of frames, and number of frames.
    public Hero(Bitmap res, int w , int b, int numFrames) {



    } // end of the hero constructor

    public void update(){}

    public void draw(Canvas canvas){}
}
