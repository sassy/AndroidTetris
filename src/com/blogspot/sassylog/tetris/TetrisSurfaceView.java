package com.blogspot.sassylog.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class TetrisSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String LOGTAG = "Tetris";
    private Thread mThread;
    private int mBlockWidth;
    private int mBlockHeight;
    private int[][] mMap = new int[22][12];
    private TetrisBlock mBlock;
    
    public TetrisSurfaceView(Context context) {
        super(context);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int displayWidth = windowManager.getDefaultDisplay().getWidth();
        int displayHeight = windowManager.getDefaultDisplay().getHeight();
        mBlockWidth = displayWidth / 10;
        mBlockHeight = displayHeight / 20;
//        X = Y = 1;
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 12; j++) {
                if (i == 0 || i == 15 || j == 0 || j == 11) {
                    Log.d(LOGTAG, "init 1");
                    mMap[i][j] = 1;
                } else {
                    mMap[i][j] = 0;
                }
            }
        }
        mBlock = new TetrisBlock(mBlockWidth, mBlockHeight);
        getHolder().addCallback(this);
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mThread != null) {
            mThread.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new Thread(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThread = null;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (canMove(1, 0)) {
                mBlock.moveToRight();
                doDraw();
            }
            return true;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (canMove(-1, 0)) {
                mBlock.moveToLeft();
                doDraw();
            }
            return true;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            if (canMove(0, 1)) {
                dropBlock();
                doDraw();
            }
            return true;
        case KeyEvent.KEYCODE_DPAD_UP:
            mBlock.rotate();
            return true;
        default:
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public void run() {
        while (mThread != null) {
            doDraw();
            dropBlock();
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    
    private void doDraw() {
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setTextSize(24);
        canvas.drawText("Tetris Test", 0, paint.getTextSize(), paint);


        for (int i = 1; i < 21; i++) {
            for (int j = 1; j < 11; j++) {
                if (mMap[i][j] == 1) {
                    Paint rectPaint = new Paint();
                    rectPaint.setColor(Color.RED);
                    canvas.drawRect((j -1) * mBlockWidth, (i - 1) * mBlockHeight, j * mBlockWidth, i * mBlockHeight, rectPaint);
                }
            }
        }
        mBlock.drawBlock(canvas);

        getHolder().unlockCanvasAndPost(canvas);
    }
    
    private boolean canMove(int moveX, int moveY) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
               if (mBlock.isTile(i, j) && mMap[i+mBlock.getY()+moveY][j+mBlock.getX()+moveX] == 1) {
                    Log.d(LOGTAG, "block");
                    return false;
                }
            }
        }

        return true;
    }
    
    private void dropBlock() {
        if (canMove(0, 1)) {
            mBlock.moveToDown();
        } else {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (mBlock.isTile(i,j))
                        mMap[mBlock.getY()+i][mBlock.getX()+j] = 1;
                }
            }
            deleteLine();
            mBlock.reset();
        }
    }
    
    private boolean deleteLine() {
        for (int i = 1; i < 15; i++) {
            boolean flag = true;
            for (int j = 1; j < 11; j++) {
                if (mMap[i][j] ==0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int k = i; k > 1; k--) {
                    for (int j = 1; j < 11; j++) {
                        mMap[k][j] = mMap[k-1][j];
                    }
                }
            }
            
        }
        return true;
    }

}
