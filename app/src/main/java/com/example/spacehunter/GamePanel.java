package com.example.spacehunter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;


// this is the screen of your device.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Random rand = new Random();

    // Set the screen width and height in our game panel.
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    // Bitmap reference to the hearts
    Bitmap hearta;
    Bitmap heartb;
    Bitmap heartc;
    private int hearts = 3;

    // Bitmap reference to the money
    Bitmap moneyBonus;
    public int heroMoney;
    public long bonusStartTime;
    private ArrayList<Bonus> myMoney;


    // Game Music and Sound
    MediaPlayer mp;
    SoundPool coinsound; //coin
    int coinsoundid;
    SoundPool bulletfiresound; // bullet
    int bulletfiresoundid;
    SoundPool enemydeathsound; // enemy
    int enemydeathsoundid;


    //optionspanel
    Bitmap myPanel;



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

    // BORDER
    private ArrayList<Borderbottom> botborder;
    private long borderStartTime;
    private ArrayList<Bordertop> topborder;

    // Obstacle
    private  ArrayList<Obstacle> obstacle;
    private long obstacleStartTime;

    // Explosion
    private Explosion explosion;

    // best score?
    private int best;


    // The thread reference
    private MainThread thread;

    //Variables to reset teh game
    private boolean newGameCreated;

    private long startReset;

    private boolean reset;

    private boolean disappear;

    private boolean started;






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


        // game music
        mp = MediaPlayer.create(context, R.raw.arcademusicloop);
        // coin sound
        coinsound = new SoundPool(99, AudioManager.STREAM_MUSIC, 1); // audio object
        coinsoundid = coinsound.load(context, R.raw.pickedcoin, 1); // load the sound

        // enemy sound
        enemydeathsound = new SoundPool(99, AudioManager.STREAM_MUSIC, 1); // audio object
        enemydeathsoundid = enemydeathsound.load(context, R.raw.explosion, 1); // load the sound

        // bullet sound
        bulletfiresound = new SoundPool(99, AudioManager.STREAM_MUSIC, 1); // audio object
        bulletfiresoundid = bulletfiresound.load(context, R.raw.bulletfire, 1); // load the sound




        // we will use the callback holder to intercept events.
        getHolder().addCallback(this);


        // we will make the GamePanel class' focus to handle events.
        setFocusable(true);
    }


    //Callback Methods
    // This is called immediately after the surface is first created.
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        // creating a new MainThread Object
        thread = new MainThread(getHolder(), this);

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

        // create borders
        botborder = new ArrayList<Borderbottom>();
        //topborder = new ArrayList<Bordertop>();
        borderStartTime = System.nanoTime();

        // create obstacle
        obstacle = new ArrayList<Obstacle>();
        obstacleStartTime = System.nanoTime();

        // create money on screen
        myMoney = new ArrayList<Bonus>();
        bonusStartTime = System.nanoTime();


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
        int counter = 0;
        // The join() method is used to hold the execution of the currently running thread until
        // the specified thread is dead.
        while(retry && counter < 1000) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }

    // know if we touch the screen or not.
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) { // pressing down?
            if(!hero.getPlaying() && newGameCreated && reset) {
                hero.setPlaying(true);
                hero.setUp(true);
            }
            if(hero.getPlaying()) {
                if(!started) {
                    started = true;
                }
                reset = false;
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
            mp.start();
            bg.update(); // calls background's update
            hero.update(); // calls hero's update


            // coin update
            long bonusTime = (System.nanoTime() - bonusStartTime)/1000000;

            if(bonusTime > (3000 - hero.getScore()/4)) {
                myMoney.add(new Bonus(BitmapFactory.decodeResource(getResources(), R.drawable.money), WIDTH + 1, (int)(rand.nextDouble() * (HEIGHT -200)), 30, 30, 1));
                bonusStartTime = System.nanoTime();
            }// end of if

            for(int i = 0; i < myMoney.size(); i++) {
                myMoney.get(i).update();
                // if hero collides wiht coin
                if(collision(myMoney.get(i), hero)) {
                    //coin sound
                    coinsound.play(coinsoundid, 5, 5, 1, 0 , 1);

                    myMoney.remove(i);
                    heroMoney += 100;
                    break;
                }

                // if coin leaves screen
                if(myMoney.get(i).getX()<-100) {
                    myMoney.remove(i);
                    break;
                }
            } // end for





            // obstacle update // bottom border update ===================================================================================
            long obstacleElapsed = (System.nanoTime() - obstacleStartTime)/1000000;

            if(obstacleElapsed > (15000 - hero.getScore()/4)) {
                obstacle.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle), WIDTH + 10, HEIGHT -290 + rand.nextInt(150), 90, 300, hero.getScore(), 1));
                obstacleStartTime = System.nanoTime();
            }

            for(int i = 0; i < obstacle.size(); i++) {
                obstacle.get(i).update();
                if(collision(obstacle.get(i), hero)) {
                    enemydeathsound.play(enemydeathsoundid, 5, 5, 1, 0 , 1);
                    hero.setPlaying(false);
                }
                break;
            }


            // bottom border update ===================================================================================
            long borderElapsed = (System.nanoTime()-borderStartTime)/1000000;

            if(borderElapsed >100 ) {                                                                                                // random y length
                botborder.add(new Borderbottom(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom), WIDTH + 10, ((HEIGHT -40)+rand.nextInt(10))));
                botborder.add(new Borderbottom(BitmapFactory.decodeResource(getResources(), R.drawable.bordertop), WIDTH + 10, ((HEIGHT -600)+rand.nextInt(10))));
                //topborder.add(new Bordertop(BitmapFactory.decodeResource(getResources(), R.drawable.bordertop), WIDTH + 10, ((HEIGHT -80)+rand.nextInt(10)), HEIGHT));
                //reset timer
                borderStartTime = System.nanoTime();
            }//end if

            //loop through every border block and check collision and remove
            for(int i = 0; i<botborder.size();i++) {
                //update obstacle
                botborder.get(i).update();

                if (collision(botborder.get(i), hero)) {
                    enemydeathsound.play(enemydeathsoundid, 5, 5, 1, 0 , 1);
                    hero.setPlaying(false);
                    break;
                }

                //if statement to remove border if is of the screen limits
                if( botborder.get(i).getX()< -20)
                {
                    botborder.remove(i);
                }
            }//end of bot border
            //loop through every border block and check collision and remove
            /*
            for(int i = 0; i<topborder.size();i++) {
                //update obstacle
                topborder.get(i).update();

                if (collision(topborder.get(i), hero)) {
                    hero.setPlaying(false);
                    break;
                }

                //if statement to remove border if is of the screen limits
                if( topborder.get(i).getX()< -20)
                {
                    topborder.remove(i);
                }
            }//end of top border*/


            // bullet update ====================================================================================================
            long bulletTimer = (System.nanoTime() - bulletStartTime)/1000000;

            // check the delay among bullets fire from the hero.
            // simply put when a bullet appears and everysecond our next bullet is faster than the previous one.
            // ie higher our score, the faster we fire.
            if(bulletTimer > (2500 - hero.getScore()/4)) {
                // position the bullet spawn
                bulletfiresound.play(bulletfiresoundid, 5, 5, 1, 0 , 1);
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
                alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy), WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - 35)), 40, 60, hero.getScore(), 3));

                // reset timer
                alienStartTime = System.nanoTime();
            }

            // updating our enemy
            for(int i = 0; i < alien.size(); i++) {
                //update the alien
                alien.get(i).update();

                // collision with object hero.
                if(collision(alien.get(i), hero)) {

                    // remove alien
                    alien.remove(i);

                    hearts--;
                    // end game
                    //hero.setPlaying(false);

                    break;
                } // end of alien hero collision

                // collision with bullet
               for(int j = 0; j < bullet.size(); j++) {
                   if(collision(alien.get(i), bullet.get(j))) {

                       // enemy death sound
                       enemydeathsound.play(enemydeathsoundid, 5, 5, 1, 0 , 1);
                       explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), alien.get(i).getX(), alien.get(i).getY(), 125, 100, 12);

                       alien.remove(i);
                       bullet.remove(j);

                       best += 30;

                       break;
                   }
                   explosion.update();
                   bullet.get(j).update();
               } // end of alien bullet collision

                // remove and alien if it is off the screen.
                if(alien.get(i).getX() < -25) {
                    alien.remove(i);
                    break;
                }
            } // end of the enemy
        } // end of if playing if
        else {
            hero.resetDYA();
            if(!reset) {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;


                // initiate explosion
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), hero.getX(), hero.getY(), 125, 100, 12);
            }

            explosion.update();

            // else reset game
            long resetElapsed = (System.nanoTime() - startReset)/1000000;

            if(resetElapsed > 2000 && !newGameCreated) {
                heroMoney = 0;
                newGame();
            }


        }// end of if playing else
    } // end of GamePanel update()


    public boolean collision(GameObject a, GameObject b) {
        if(Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }







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
            if(!disappear) {
                hero.draw(canvas);
            }
            // draw enemy
            for(Bullet fp: bullet) {
                fp.draw(canvas);
            }

            // draw enemy
            for(Enemy aln: alien) {
                aln.draw(canvas);
            }

            for(Obstacle obst: obstacle) {
                obst.draw(canvas);
            }

            // draw bottom border
            for(Borderbottom brb: botborder) {
                brb.draw(canvas);
            }
            // mbg is moneybag
            for(Bonus mbg: myMoney) {
                mbg.draw(canvas);
            }

            if(started) {
                explosion.draw(canvas);
            }



            drawText(canvas);

            canvas.restoreToCount(savedState);
        }


    } // end of the draw method

    public void newGame() {

        disappear = false;
        alien.clear();
        obstacle.clear();
        botborder.clear();
        bullet.clear();
        myMoney.clear();


        hero.resetDYA();
        hero.resetScore();
        hero.setY(HEIGHT/2);

        newGameCreated = true;

    } // end of newGame() method


    public void drawText(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Distance: " + (hero.getScore()*2), 10, HEIGHT - 10, paint);
        canvas.drawText("Score: " + best, WIDTH - 215, HEIGHT - 10, paint);

        moneyBonus = BitmapFactory.decodeResource(getResources(), R.drawable.money);
        canvas.drawBitmap(moneyBonus, 0, 60, null);
        canvas.drawText("$" + heroMoney, 40, 85, paint);


        if(hearts == 3) {
            hearta = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            canvas.drawBitmap(hearta, 0, 0, null);
            heartb = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            canvas.drawBitmap(heartb, 40, 0, null);
            heartc = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            canvas.drawBitmap(heartc, 80, 0, null);
        }
        if(hearts == 2) {
            hearta = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            canvas.drawBitmap(hearta, 0, 0, null);
            heartb = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            canvas.drawBitmap(heartb, 40, 0, null);
        }
        if(hearts == 1) {
            hearta = BitmapFactory.decodeResource(getResources(), R.drawable.life);
            canvas.drawBitmap(hearta, 0, 0, null);
        }
        if(hearts == 0) {
            hero.setPlaying(false);
            hearts = 3;
        }

        // panel of options
        if(!hero.getPlaying() && newGameCreated && reset) {
            //create object
            Paint paint1 = new Paint();
            paint1.setTextSize(25);
            paint1.setColor(Color.WHITE);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            // draw picture
            myPanel = BitmapFactory.decodeResource(getResources(), R.drawable.options);
            canvas.drawBitmap(myPanel, WIDTH/2 -200, HEIGHT/2 -120, null);

            canvas.drawText("PRESS TO START", WIDTH/2 - 100, HEIGHT/2, paint1);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2 -160, HEIGHT/2 +40 , paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2 - 120, HEIGHT/2 +80, paint1);

        }


    } // end of drawText()

} // end of gamePanel class
