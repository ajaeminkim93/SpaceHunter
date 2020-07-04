package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    // Bitmap part of the android.graphics.Bitmap class represents a bitmap image.
    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res) {
        // set image to the result in the background class
        image = res;

    } // end of constructor

    public void update() {
        // If we want our background image to scroll to the left every time our game is updated
        // With that sceptic we will give the illusion of continuous bg and movements.

        // change the x coordinate of our background.
        x = x + dx;

        // check if background is moving out of the screen
        if(x<-GamePanel.WIDTH) { // GamePanel.WIDTH represents the width of our screen.

            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        // draw the first background to the screen.
        canvas.drawBitmap(image, x, y, null);

        // if our background is off the x limit of our screen, add another background behind it.
        if(x < 0) {
            canvas.drawBitmap(image, x + GamePanel.WIDTH, y, null);
        }

    } // end of draw method.


    // new method for dx
    // we need a method to check the value of the new x coordinate
    public void setVector(int dx) {
        this.dx = dx;
    }


} // end of background class
