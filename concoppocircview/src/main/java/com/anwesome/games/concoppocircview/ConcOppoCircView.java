package com.anwesome.games.concoppocircview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anweshmishra on 19/07/17.
 */

public class ConcOppoCircView extends View {
    private int n=3,w,h,time = 0;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public void setN(int n) {
        this.n = Math.max(n,this.n);
    }
    public ConcOppoCircView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(w/50);
        }
        time++;
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return true;
    }
    private class ConcOppoCirc {
        public void draw(Canvas canvas,float scale) {
            for(int i=0;i<n;i++) {
                float sf = scale;
                if(i%2 == 1) {
                    sf = 1-scale;
                }
                canvas.save();
                canvas.translate(w/2,h/2);
                int r = (w/3)/(i+1);
                paint.setColor(Color.GRAY);
                canvas.drawCircle(0,0,r,paint);
                paint.setColor(Color.parseColor("#01579B"));
                canvas.drawArc(new RectF(-r,-r,r,r),0,360*sf,false,paint);
                canvas.restore();
            }
        }
    }
    private class AnimationHandler {
        private boolean animated = false;
        private ConcOppoCirc concOppoCirc = new ConcOppoCirc();
        public void animate() {
            if(animated) {
                try {
                    Thread.sleep(75);
                    invalidate();
                }
                catch (Exception ex) {

                }
            }
        }
        public void draw(Canvas canvas) {
            concOppoCirc.draw(canvas,1);
        }
        public void startAnimation() {
            if(!animated) {
                animated = true;
                postInvalidate();
            }
        }
    }
    private class StateContainer {
        private float scale = 0,dir = 0;
        public void update() {
            scale += 0.2f*dir;
            if(scale > 1 || scale <0) {
                dir = 0;
                scale = (scale > 1)?1:0;
            }
        }
        public void startUpdating() {
            if(dir == 0) {
                dir = scale <= 0 ?1:-1;
            }
        }
        public boolean stopped() {
            return dir == 0;
        }
    }
}
