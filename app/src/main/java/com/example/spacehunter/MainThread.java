package com.example.spacehunter;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    // Variables we need for our thread.

    private int FPS = 30;

    // Need to know if our fps is too slow.
    private double averageFPS;

    private SurfaceHolder surfaceHolder;

    // referencing GamePanel
    private GamePanel gamePanel;

    // To know if our thread is running.
    private boolean running;

    // Canvas object helps us draw something onto our screen.
    public static Canvas canvas;


    // Constructor for mainthread.
    // we need references of our Content view objects.
    // this is so our thread knows what is going on.
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

    }

    // Now we need to override the run method.
    // All threads have a run method.
    // So to take advantage of the thread we will write out time code inside the run method.
    // Thread just wants to know when an action has occured in the game.

    public void run() { // run method is the main() to the thread.
        // every second should be 30 frames.

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;

        int frameCount = 0;

        // Target Time
        // 1 second = 1000 milliseconds
        // 1000 milliseconds / 30 frames = 33.3 milliseconds per frame.
        long targetTime = 1000/FPS;

        while(running) {
            // first set the time.
            startTime = System.nanoTime();

            // first our canvas is blank;
            canvas = null;
            // we need canvas to paint objects to the screen on every frame.
            // develop canvas code inside try/catch to catch errors.


            try {
                // lock the canvas to our content view.
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    // draw repeatedly like a flip book
                    this.gamePanel.draw(canvas);

                } // end of synchronized
            } catch (Exception e) {
            } // end of try / catch
            finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            timeMillis = (System.nanoTime() - startTime) / 1000000;

            // Time that we wait between the frames when the next frame is going to enter the loop.
            waitTime = targetTime - timeMillis;

            // while we wait, the game is paused.
            // wait time is so short its not noticed.

            try{
                this.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            // when framecount is == 30 get average fps
            if(frameCount == FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("Average Frames Per Second " + averageFPS);
            }




        } // end of while loop
    } // end of run method


    public void setRunning(boolean b) {
        running = b;
    }


} // end of thread class
