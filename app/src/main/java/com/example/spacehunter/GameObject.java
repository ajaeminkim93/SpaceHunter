package com.example.spacehunter;

import android.graphics.Rect;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    // create the setters and getters
    // x setter
    public void setX(int x) {
        this.x = x;
    }
    // x getter
    public int getX() {
        return x;
    }

    // y setter
    public void setY(int y) {
        this.y = y;
    }
    // y getter
    public int getY() {
        return y;
    }

    // dont needd setters for height and width because they are already defined with Gimp
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }


    // create the rectangle method that creates a rectangle around to image.
    // it will ch eck for collisions.
    public Rect getRectangle() {
        return new Rect(x, y, x+width,  y+height);
    }


} // end of game object class
