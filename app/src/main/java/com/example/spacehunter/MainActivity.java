package com.example.spacehunter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;



// MainActvity = First class that is run in the project.
//  |
//  V  *MainActivity points to the GamePanel class not the the XML file.
// GamePanel.class
public class MainActivity extends AppCompatActivity {

    // Step1: Create classes.
    GamePanel gameView; // Our game view that extends the SurfaceView
    FrameLayout game;   // We use this layout as a holder between the SurfaceView and the Relative Layout
                        // with the buttons or for everything we are placing.
    RelativeLayout GameButtons; // Holder for the Buttons.

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Step2: instantiate objects.
        // Create screen objects.
        gameView = new GamePanel(this);         // holds all the animations
        game = new FrameLayout(this);           // holds the other two layouts
        GameButtons = new RelativeLayout(this); // outer most layer holds buttons


        // Step3: Create Buttons
        // Button 1
        Button buttonOne = new Button(this );
        //buttonOne.setText("Button1");
        //buttonOne.setId(123456); // Set an ID for the button because the default new buttons have the same id number.

        // Button 2
        Button buttonTwo = new Button(this );
        //buttonTwo.setText("Button2");
        //buttonTwo.setId(789111); // Set an ID for the button because the default new buttons have the same id number.

        // Step4: Add buttons to relative layout.
        // Since now we have two different buttons. We need to add them into the Relative Layout with the help of the
        // second layout, Framelayout in a SurfaceView.

        // Button 1 width height
        // First we need to define the layout parameter.
        // Creating an object that holds width and height in the relative layout for the first button/
        RelativeLayout.LayoutParams b1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Button 2 width height
        RelativeLayout.LayoutParams b2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Now the we wrap the content of the buttons, we need next to define the relativeLayout width and height that we will add to the buttons
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        // Now we set this match parent layout to the Relativelayout that we named as GameButtons.
        GameButtons.setLayoutParams(params);

        // Then add buttons to this layout
        //GameButtons.addView(buttonOne);
        //GameButtons.addView(buttonTwo);

        // Up until here we created 2 buttons in a RelativeLayout but we dont know exactly where we place them.
        // So now we define where for button 1
        b1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        b1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        buttonOne.setLayoutParams(b1);

        // Now we do same for button 2
        b2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        b2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        buttonTwo.setLayoutParams(b2);




        // Turn the title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set game to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // NOw we need to add this RelativeLayout with the buttons in the Surface View.
        // We created the relative layout.
        // We will use the frame layout as the third player here as a referee.
        game.addView(gameView);
        game.addView(GameButtons);

        // new Content view. The new ContentView is a FrameLayout that holds a surfaceView and a relative layout
        setContentView(game);

        //setContentView(new GamePanel(this)); // This directs us the GamePanel.class
        //setContentView(R.layout.activity_main);
    }
}