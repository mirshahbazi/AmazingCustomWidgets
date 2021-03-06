package com.anwesome.ui.footiepitchbutton;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by anweshmishra on 06/04/17.
 */
public class FootiePitch {
    private float x,y,size,deg = 0,dir = 0;
    public FootiePitch(float x,float y,float size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(x,y);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#43A047"));
        canvas.drawRect(new RectF(-size,-size,size,size),paint);
        canvas.rotate(deg);
        paint.setStrokeWidth(size/30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawRect(new RectF(-size/2,-size/4,size/2,size/4),paint);
        canvas.drawRect(new RectF(-size/4,size/4-size/8,size/4,size/4),paint);
        canvas.drawArc(new RectF(-size/4,-size/4-size/8,size/4,-size/4+size/8),180,180,true,paint);
        canvas.restore();
    }
    public void update() {
        deg+=dir*20;
        if(deg == 180 || deg == 0) {
            dir = 0;
        }
    }
    public void startMoving() {
        dir = deg == 0?1:-1;
    }
    public boolean stop() {
        return dir == 0;
    }

}
