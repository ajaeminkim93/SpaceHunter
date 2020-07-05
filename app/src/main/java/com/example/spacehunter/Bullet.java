package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet extends GameObject { // need to extend gameobject for coordinates and hitbox

    private int speed; // speed of the bullet.

    // to animate the bullet image.
    private Animation animation = new Animation();

    // bitmap reference to the image
    private Bitmap spritesheet;



    public Bullet(Bitmap res, int x, int y, int w, int h, int numFrames) {
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        // initial speed of the bullet.
        speed = 13;

        // the frames of our bullet image.
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        // this loop saves all the frames of the bullet image into a new table.
        // where image.length is the number for frames, past each image into the frames.
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        // do the animation. since everything is ready.
        animation.setFrames(image);

        // delay of the animation per frame.
        animation.setDelay(120-speed);
    }

    public void update() {
        x += speed -4;
        animation.update();
    } // end of update

    public void draw(Canvas canvas) {
        // draw image onto our canvas
        try {
            canvas.drawBitmap(animation.getImage(), x-30, y, null);
        } catch (Exception e) {

        }
    } // end of draw
}
