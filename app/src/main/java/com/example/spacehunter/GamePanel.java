package com.example.spacehunter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;


// this is the screen of your device.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Random rand = new Random();

    // Set the screen width and height in our game panel.
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    public static final int MOVESPEED = -5;



    // BACKGROUND
    // Need reference to the background class so that two classes can communicate.
    private Background bg; // we create the body of the background class inside GamePanel inside surface created method.

    //HERO
    // Need reference to the hero class so that the two classes can communicate.
    private Hero hero;

    // BULLET VARS:
    private ArrayList<Bullet> bullet;
    private long bulletStartTime;

    // ENEMY
    private ArrayList<Enemy> alien;
    private long alienStartTime;


    //test
    private Background bgAlt;

    // The thread reference
    private MainThread thread;

    // GamePanel Constructor
    public GamePanel(Context context) {
        // Allows access to application-specific resources and classes, as well as up-calls for
        // application-level operations such as launching activities, broadcasting and receiving intents, etc.
        // Instances of the class android.content.Context provide the connection to the Android
        // system which executes teh application.

        // It is the context of the current state of the application/object
        // It lets newly created objects understand what has been going on.
        // Typically you call it to get information regarding another portion of your program.

        super(context);

        // we will use the callback holder to intercept events.
        getHolder().addCallback(this);

        // creating a new MainThread Object
        thread = new MainThread(getHolder(), this);

        // we will make the GamePanel class' focus to handle events.
        setFocusable(true);
    }


    //Callback Methods
    // This is called immediately after the surface is first created.
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        // create object in surface created.
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundspacee)); // background is our png image.
        //bgAlt = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundspace));
        //lets set out dx -5 so our bg image slowly moves off the screen with -5 speed.
        // To do so we use the setVector method from background class.
        //bg.setVector(-3);

        hero = new Hero(BitmapFactory.decodeResource(getResources(), R.drawable.hero), 30, 45, 3);

        // create bullet array and set timer to system time
        bullet = new ArrayList<Bullet>();
        bulletStartTime = System.nanoTime();

        // create enemy object and set timer to system time
        alien = new ArrayList<Enemy>();
        alienStartTime = System.nanoTime();

        // start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;

        // The join() method is used to hold the execution of the currently running thread until
        // the specified thread is dead.
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // know if we touch the screen or not.
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) { // pressing down?
            if(!hero.getPlaying()) {
                hero.setPlaying(true);
            } else {
                hero.setUp(true);
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP) { // release screen press?
            hero.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    // Update Method.
    public void update() {
        if(hero.getPlaying()) {
            bg.update(); // calls background's update
            hero.update(); // calls hero's update

            // bullet update ====================================================================================================
            long bulletTimer = (System.nanoTime() - bulletStartTime)/1000000;

            // check the delay among bullets fire from the hero.
            // simply put when a bullet appears and everysecond our next bullet is faster than the previous one.
            // ie higher our score, the faster we fire.
            if(bulletTimer > (2500 - hero.getScore()/4)) {
                // position the bullet spawn
                bullet.add(new Bullet((BitmapFactory.decodeResource(getResources(), R.drawable.bullet)), hero.getX() + 60, hero.getY() + 24, 15, 12, 4));
                bulletStartTime = System.nanoTime();
            }

            // this is a common for loop to animate and update the frames of the bullet image.
            for(int i = 0; i < bullet.size(); i++) {
                bullet.get(i).update();
                // remove bullet if off the screen.
                if(bullet.get(i).getX()<-10) {
                    bullet.remove(i);
                }
            } // end of the bullet


            // enemy update ====================================================================================================
            long alienTimer = (System.nanoTime() - alienStartTime)/1000000;

            // spawn first enemy in 10 seconds and then more often later
            if(alienTimer > (10000 - hero.getScore()/4)) {

                alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy), WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - 50)), 40, 60, hero.getScore(), 3));

                // reset timer
                alienStartTime = System.nanoTime();
            }

            for(int i = 0; i < alien.size(); i++) {
                //update the alien
                alien.get(i).update();

                // remove and alien if it is off the screen.
                if(alien.get(i).getX() < -25) {
                    alien.remove(i);
                    break;
                }
            } // end of the enemy
        } // end of if playing
    } // end of GamePanel update()



    // creating GamePanel's draw() method.
    @Override
    public void draw(Canvas canvas) {

        // need to scale our images for all devices and screens.
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            // draw background
            bg.draw(canvas);

            // draw hero
            hero.draw(canvas);

            // draw enemy
            for(Bullet fp: bullet) {
                fp.draw(canvas);
            }

            // draw enemy
            for(Enemy aln: alien) {
                aln.draw(canvas);
            }


            canvas.restoreToCount(savedState);
        }


    } // end of the draw method



} // end of gamePanel class
