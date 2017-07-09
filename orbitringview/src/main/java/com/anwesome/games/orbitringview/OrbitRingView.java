package com.anwesome.games.orbitringview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anweshmishra on 10/07/17.
 */

public class OrbitRingView extends View {
    private int n=3;
    private Renderer renderer = new Renderer();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public OrbitRingView(Context context) {
        super(context);
    }
    public void setN(int n) {
        this.n = Math.max(this.n,n);
    }
    public void onDraw(Canvas canvas) {
        renderer.render(canvas);
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            renderer.handleTap(event.getX(),event.getY());
        }
        return true;
    }
    private class Renderer {
        private int w,h,time = 0;
        private DrawingService drawingService;
        public void render(Canvas canvas) {
            if(time == 0) {
                w = canvas.getWidth();
                h = canvas.getHeight();
                drawingService = new DrawingService(w,h);
            }
            drawingService.draw(canvas);
            time++;
        }
        public void handleTap(float x,float y) {
        }
    }
    private class DrawingService {
        public DrawingService(int w,int h) {

        }
        public void draw(Canvas canvas) {

        }
    }
    private class AnimationService {
        private boolean animated = false;
        public void animate() {
            if(animated) {
                try {
                    Thread.sleep(50);
                    invalidate();
                }
                catch (Exception ex) {

                }
            }
        }
        public void handleTap(float x,float y) {
            boolean tapped = false;
            if(!animated && tapped) {
                animated = true;
                postInvalidate();
            }
        }
    }
    private class OrbitRing {
        private int index = 0;
        private float x,y,r,scale = 0,dir = 0;
        public OrbitRing(int index,float size) {
            this.index = index;
            this.r = size*(index+1);
            this.x = 0;
            this.y = -this.r;
        }
        public void draw(Canvas canvas) {
            float deg = 360*scale;
            canvas.save();
            canvas.translate(canvas.getWidth()/2,canvas.getHeight()/2);
            canvas.save();
            canvas.rotate(deg);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
            canvas.drawCircle(this.x,this.y,r/8,paint);
            canvas.restore();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(r/60);
            paint.setColor(Color.GRAY);
            canvas.drawCircle(0,0,r,paint);
            paint.setColor(Color.BLUE);
            Path path = new Path();
            for(float i=0;i<deg;i+=10) {
                float x= (float)(r*Math.cos(i*Math.PI/180)),y = (float)(r*Math.sin(i*Math.PI/180));
                if(i == 0) {
                    path.moveTo(x,y);
                }
                else {
                    path.lineTo(x,y);
                }
            }
            canvas.drawPath(path,paint);
            canvas.restore();
        }
        public void update() {
            scale += 0.2f*dir;
            if(scale > 1) {
                dir = 0;
                scale = 1;
            }
            if(scale < 0) {
                dir = 0;
                scale = 0;
            }
        }
        public boolean stopped() {
            return dir == 0;
        }
        public int hashCode() {
            return index;
        }
        public boolean handleTap(float x,float y) {
            boolean condition =  x>=this.x-r/8 && x>=this.x+r/8 && y>=this.y-r/8 && y<=this.y+r/8 && dir == 0;
            if(condition) {
                dir = scale >=0?1:-1;
            }
            return condition;
        }
    }
}
