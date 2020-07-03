package com.example.spacehunter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;



// this is the screen of your device.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

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
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    // Update Method.
    public void update() {

    }

} // end of gamePanel class
