package com.example.spacehunter;


import android.graphics.Bitmap;

// the animation class will help us to animate the image through their sprites.
// we will create our own animation class and not animation from the android package.
public class Animation {

    // a bitmap table keeps the index of the image frames [0]frame [1]frame [2]frame
    private Bitmap[] frames;

    // we need also to know in which frame of the image we are
    private int currentFrame;

    // we need a timer for the animation
    private long startTime;

    // and a delay (the delay between frames determines how fast our animation is)
    private long delay;

    // need boolean for each image, to determine if something happens to the image.
    private boolean playedOnce;



    public void setFrames(Bitmap[] frames) {
        // get the frame images.
        this.frames = frames;

        // every image will start from the 1st frame/sprite
        currentFrame = 0;

        startTime = System.nanoTime();

    } // end of setFrames


    // Now we need the setters for the delay and the frames.
    public void setDelay(long d) {
        delay = d;
    }
    public void setFrame(int i) {
        currentFrame = i;
    }

    // need an update() method for the animation class as well.
    // The timer determines which frame of the image is gonna be returned.
    // In general timers are used to determine when an object will appear.
    // All objects have a timer.
    public void update() {
        long elapsed = (System. nanoTime() - startTime)/1000000;

        // if we set the delay of the animation to 10 miliseconds, we want to spawn the animation after 10miliseconds of starting the game.
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    } // end of update


    // Determines what the hero() class will draw.
    public Bitmap getImage() {
        return frames[currentFrame];
    }

    // get the frames and get the boolean value of playedOnce
    public int getFrame() {
        return currentFrame;
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }



}
