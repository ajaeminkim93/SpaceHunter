package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemy extends GameObject {

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


    public Enemy(Bitmap ref, int x, int y, int w, int h, int numFrames) {
        // super class of GameObject
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 4 + (int)(rand.nextDouble()*score/30);
        if(speed > 40) {
            speed = 40;
        }

        // we create a bitmap object so we can save later all the versions of our image.
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;




    } // end of enemy class

    public void update(){}

    public void draw(Canvas canvas){}
}
