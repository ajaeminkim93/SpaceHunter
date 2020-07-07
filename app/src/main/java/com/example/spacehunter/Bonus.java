package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Bonus extends GameObject {

    // variables
    private int score;
    // speed of the enemy
    private int speed;
    // enemies have different speed, and spawns?
    private Random rand = new Random();
    // animates the object
    private Animation animation = new Animation();
    // bitmap reference to our image
    private Bitmap spritesheet;


    public Bonus(Bitmap res, int x, int y, int w, int h, int numFrames) {
        // super class of GameObject
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        speed = 4 + (int)(rand.nextDouble()*score/30);
        if(speed > 40) {
            speed = 40;
        }

        // we create a bitmap object so we can save later all the versions of our image.
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        // loop to save all the image frames into out bitmap table.
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        // now we have all info and can animte.
        animation.setFrames(image);
        animation.setDelay(100 - speed);



    } // end of enemy class

    public void update() {
        x -= speed;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try{
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e) {}
    }


    @Override
    public int getWidth() {
        return width - 10;
    }

}
