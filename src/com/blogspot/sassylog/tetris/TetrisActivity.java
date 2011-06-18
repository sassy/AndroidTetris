package com.blogspot.sassylog.tetris;

import android.app.Activity;
import android.os.Bundle;

public class TetrisActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TetrisSurfaceView(this));
        
    }
}