package com.blogspot.sassylog.tetris;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TetrisBlock {
    private int[][][] mBlock = new int[4][4][4];
    private int mX;
    private int mY;
    private int mBlockWidth;
    private int mBlockHeight;
    private int mRotate;

    public TetrisBlock(int blockWidth, int blockHeight) {
        mBlockWidth = blockWidth;
        mBlockHeight = blockHeight;
        
        mBlock[0][0][0] = 1;
        mBlock[0][1][0] = 1;
        mBlock[1][1][0] = 1;
        mBlock[1][2][0] = 1;
        
        mBlock[0][1][1] = 1;
        mBlock[1][0][1] = 1;
        mBlock[1][1][1] = 1;
        mBlock[2][0][1] = 1;
        
        mX = mY = 1;
    }
    
    public boolean isTile(int x, int y) {
        if (mBlock[x][y][mRotate] == 0)
            return false;
        else
            return true;
    }
    
    public void rotate() {
        mRotate = (++mRotate %2);
    }
    
    public void moveToRight() {
        mX++;
    }

    public void moveToLeft() {
        mX--;
    }

    public void moveToDown() {
        mY++;
    }
    
    public void reset() {
        mX = mY = 1;
        mRotate = 0;
    }

    public int getX() {
        return mX;
    }
    
    public int getY() {
        return mY;
    }

    public void drawBlock(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (mBlock[i][j][mRotate] == 1) {
                    Paint rectPaint = new Paint();
                    rectPaint.setColor(Color.BLUE);
                    canvas.drawRect((j - 1 + mX) * mBlockWidth, (i - 1 + mY) * mBlockHeight, (j + mX) * mBlockWidth, (i + mY) * mBlockHeight, rectPaint);
                }
            }
        }

    }

}
