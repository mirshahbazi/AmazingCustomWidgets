package com.anwesome.ui.lineanddot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by anweshmishra on 06/06/17.
 */

public class LineAndDotView extends View{
    private int time = 0,w,h,n=4;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<LineAndDot> lineAndDots = new ArrayList<>();
    public LineAndDotView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
        }
        paint.setStrokeWidth(Math.max(w,h)/100);
        time++;
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return true;
    }
    private class AnimationHandler {
        private ConcurrentLinkedQueue<LineAndDot> activeLineDots = new ConcurrentLinkedQueue<>();
        private boolean isAnimated = false;
        public void animate() {
            if(isAnimated) {
                for(LineAndDot lineAndDot:activeLineDots) {
                    lineAndDot.update();
                    if(lineAndDot.stopped()) {
                        activeLineDots.remove(lineAndDot);
                        if(activeLineDots.size() == 0) {
                            isAnimated = false;
                        }
                    }
                }
                try {
                    Thread.sleep(50);
                    invalidate();
                }
                catch (Exception ex) {

                }
            }
        }
        public void handleTap(float x,float y) {
            for(LineAndDot lineAndDot:lineAndDots) {
                if(lineAndDot.handleTap(x,y)) {
                    lineAndDot.startMoving();
                    activeLineDots.add(lineAndDot);
                    if(activeLineDots.size() == 1) {
                        isAnimated = true;
                        postInvalidate();
                    }
                    break;
                }
            }
        }
    }
    private class Line {
        private float y,x,lx = 0;
        private boolean stopped = true;
        public Line(float y) {
            x = w/10;
            this.y = y;
        }
        public void startMoving() {
            if(stopped) {
                stopped = false;
            }
        }
        public void update(float dir) {
            lx += 0.16f*dir;
            if(lx>=0.8f*w && lx<=0) {
                stopped = true;
            }
        }
        public boolean isStopped() {
            return stopped;
        }
        public void draw(Canvas canvas) {
            canvas.drawLine(x,y,x+lx,y,paint);
        }
        public int hashCode() {
            return (int)(y+lx);
        }
    }
    private class Dot {
        private float x,y,scale = 0,r;
        private boolean stopped = true;
        public Dot(float x) {
            this.x = x;
            this.y = 9*h/10;
            this.r = h/16;
        }
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.translate(x,y);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(0,0,r,paint);
            paint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.scale(scale,scale);
            canvas.drawCircle(0,0,r,paint);
            canvas.restore();
            canvas.restore();
        }
        public void update(float dir) {
            scale += 0.2f*dir;
            if(scale>=1 && scale<=0) {
                stopped = true;
            }
        }
        public void startMoving() {
            if(stopped) {
                stopped = false;
            }
        }
        public boolean isStopped() {
            return stopped;
        }
        public int hashCode() {
            return (int)(x+scale);
        }
        public boolean handleTap(float x,float y) {
            return x>=this.x-1.5f*r && x<=this.x+1.5f*r && y>=this.y-15.f*r && y<=this.y+1.5f*r;
        }
    }
    private class LineAndDot {
        private Line line;
        private float dir = 0,prevDir = -1;
        private Dot dot;
        public boolean stopped() {
            return dir == 0;
        }
        public LineAndDot(float x,float y) {
            line = new Line(y);
            dot = new Dot(x);
        }
        public void draw(Canvas canvas) {
            line.draw(canvas);
            dot.draw(canvas);
        }
        public void startMoving() {
            dir = -1*prevDir;
        }
        public void update() {
            line.update(dir);
            dot.update(dir);
            if(line.isStopped() && dot.isStopped()) {
                prevDir = dir;
                dir = 0;
            }
        }
        public int hashCode() {
            return dot.hashCode()+line.hashCode();
        }

        public boolean handleTap(float x,float y) {
            return dot!=null && dot.handleTap(x,y);
        }
    }
}