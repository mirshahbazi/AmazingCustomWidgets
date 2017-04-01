package com.anwesome.ui.syncbutton;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by anweshmishra on 02/04/17.
 */
public class SyncShape {
    private float x,y,r,deg = 0,dir = 0;
    public SyncShape(float x,float y,float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(x,y);
        canvas.rotate(deg);
        for(int i =0;i<2;i++) {
            canvas.save();
            canvas.rotate(180*i);
            drawShape(canvas,paint);
            canvas.restore();
        }
        canvas.restore();
    }
    private void drawShape(Canvas canvas,Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(r/6);
        Path path = new Path();
        for(int i=60;i<=120;i++) {
            float x = (float)(r*Math.cos(i*Math.PI/180)),y = (float)(r*Math.sin(i*Math.PI/180));
            if(i == 60) {
                path.moveTo(x,y);
            }
            else {
                path.lineTo(x, y);
            }
        }
        drawPathInCircle(path,r-r/10,120);
        drawPathInCircle(path,r,135);
        drawPathInCircle(path,r+r/10,120);
        canvas.drawPath(path,paint);
    }
    public void update() {
        deg+=dir;
        if(deg %180 == 0) {
            dir = 0;
        }
    }
    public void startMoving() {
        dir = dir == 0?1:0;
    }
    public boolean stopped() {
        return dir == 0;
    }
    public int hashCode() {
        return (int)(x+y+dir+deg);
    }
    private void drawPathInCircle(Path path,float r,float deg) {
        float x = (float)(r*Math.cos(deg*Math.PI/180)),y = (float)(r*Math.sin(deg*Math.PI/180));
        path.lineTo(x,y);
    }
}
