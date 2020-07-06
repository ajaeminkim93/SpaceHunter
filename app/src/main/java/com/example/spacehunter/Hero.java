package com.example.spacehunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Hero extends GameObject { // hero extends the game object class

    // Hero variables
    private Bitmap spritesheet;

    private int score;

    // y value very time we touch the screen
    private double dya;

    // is hero going up or down
    private boolean up;

    // is the game started?
    private boolean playing;


    // create an Animation class within our hero class.
    private Animation animation = new Animation();

    private long startTime;



    // we need reference to the bitmap. with and height of frames, and number of frames.
    public Hero(Bitmap res, int w, int h, int numFrames) {
        // when the game starts the hero is created to the left (100x) and in the middle (HEIGHT/2)
        x = 100;
        y = GamePanel.HEIGHT / 2;

        // We also need the variable for when we touch the screen.
        dy = 0;

        // score of the player
        score = 0;

        // need height and width of the image to create a bitmap.
        height = h;
        width = w;

        // create the bitmap image
        Bitmap[] image = new Bitmap[numFrames];
        // create the sprite
        spritesheet = res;

        // THe hero image has 3 frames/sprite so [0] is the first [1] is seconds [2] is third.
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        // Now we create the animation.
        animation.setFrames(image);
        animation.setDelay(10);

        // initialize the timer so that we can use the update method.
        startTime = System.nanoTime();

    } // end of the hero constructor

    public void setUp(boolean b) {
        up = b;
    }

    public void update() {
        // timer of our hero in milliseconds
        long elapsed = elapsed = (System.nanoTime() - startTime)/1000000;

        // Now when our timer gets past 100 milliseconds we want the score to auto increment by the time.
        if(elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }

        // animate hero animation
        animation.update();


        // now we need to update the hero's movement. acceleration
        if(up) {
            dy = (int)(dya -= .7);
        } else {
            dy = (int)(dya += .7);
        }

        // set speed limit
        if(dy > 3) {
            dy = 3;
        }
        if(dy < -3) {
            dy = -3;
        }

        y += dy * 2;
        dy = 0;

    } // end of update;

    // need to draw our animation.
    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    } // end of draw

    public int getScore() {
        return score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDYA() {
        dya = 0;
    }

    public void resetScore() {
        score = 0;
    }



}
