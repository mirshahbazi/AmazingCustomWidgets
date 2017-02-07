package com.anwesome.ui.completeballbuttons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by anweshmishra on 07/02/17.
 */
public class BallButton {
    private char label;
    private int deg;
    private float x,y,r;
    private View.OnClickListener onClickListener;
    private boolean finishAnimating = false;
    private float scale = 0,scaleSpeed = 0.0f,degLabel = 0,degSpeed = 0;
    public void setR(float r) {
        this.r = r;
    }
    public BallButton(char label) {
        this.label = label;
    }
    public boolean hasFinishedAnimating() {
        return finishAnimating;
    }
    public void setSpeed(float dir) {
        finishAnimating = false;
        this.scaleSpeed = 0.1f*dir;
        this.degSpeed = 36*dir;
    }
    public void setDimensions(int deg,float pivotx,float pivoty,float r1,float r) {
        this.deg = deg;
        this.x = pivotx+(float)(r1*Math.cos(deg*Math.PI/180));
        this.y = pivoty+(float)(r1*Math.sin(deg*Math.PI/180));
        this.r = r;
        finishAnimating = true;
    }
    public int getDeg() {
        return deg;
    }
    public void draw(Canvas canvas, Paint paint,int color) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(r,r,r,paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.save();
        canvas.translate(r,r);
        canvas.rotate(degLabel);
        canvas.scale(scale,scale);
        canvas.drawCircle(0,0,r,paint);
        canvas.restore();
        scale+=scaleSpeed;
        degLabel+=degSpeed;
        if(degLabel>=360 && scale>=1.0f) {
            scale = 1.0f;
            degLabel = 360;
            degSpeed = 0;
            scaleSpeed = 0;
        }
        if(degLabel<=0 && scale <= 0) {
            scale = 0;
            degLabel = 0;
            degSpeed = 0;
            scaleSpeed = 0;
        }
    }
    public int hashCode() {
        return (int)x+(int)y+deg+label;
    }
    public boolean handleTap(float x,float y) {
        return x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}