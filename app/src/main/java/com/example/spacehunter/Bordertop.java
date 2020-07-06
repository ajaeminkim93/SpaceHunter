package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bordertop extends GameObject {

    // variables
    private Bitmap image;




    public Bordertop(Bitmap res, int x, int y, int h) {
        height = h;
        width = 20;

        // get coordinated from the super class
        this.x = x;
        this.y = y;


        // set movement equal to the game background movement speed. illusion of hero speed.
        dx = GamePanel.MOVESPEED;

        // create image, no need to loop because 1 frame
        image = Bitmap.createBitmap(res, 0, 0, width, height);


    } // end of constructor

    public void update() {
        // change border position.
        x += dx;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
}
