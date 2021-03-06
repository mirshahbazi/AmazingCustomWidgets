package com.anwesome.ui.ystbuttonlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anweshmishra on 24/05/17.
 */
public class YSTButtonView extends View {
    private OnSelectionChangeListener onSelectionChangeListener;
    public void setOnSelectionChangeListener(OnSelectionChangeListener onSelectionChangeListener) {
        this.onSelectionChangeListener = onSelectionChangeListener;
    }
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int time = 0,w,h;
    private YSTLine ystLine = new YSTLine();
    private YSTRect rect;
    private AnimationHandler animationHandler;
    public YSTButtonView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            rect = new YSTRect();
            animationHandler = new AnimationHandler();
        }
        rect.draw(canvas);
        ystLine.draw(canvas);
        time++;
    }
    public void update(float factor) {
        rect.update(factor);
        ystLine.update(factor);
        postInvalidate();
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && rect.handleTap(event.getX(),event.getY())) {
            animationHandler.start();
        }
        return true;
    }
    private class YSTRect  {
        private float x,y,size,scale = 0;
        public YSTRect() {
            x = w/2;
            y = h/2;
            size = 4*w/5;
        }
        public void draw(Canvas canvas) {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(size/20);
            canvas.save();
            canvas.translate(x-size/2,y-size/2);
            canvas.drawRoundRect(new RectF(0,0,size,size),size/10,size/10,paint);
            canvas.drawLine(size/5,3*size/5,3*size/10,3*size/5,paint);
            canvas.drawLine(4*size/10,3*size/5,9*size/10,3*size/5,paint);
            canvas.drawLine(size/5,4*size/5,6*size/10,4*size/5,paint);
            canvas.drawLine(7*size/10,4*size/5,9*size/10,4*size/5,paint);
            canvas.restore();
            paint.setColor(Color.argb(150,0,0,0));
            paint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.translate(x,y);
            canvas.scale(scale,scale);
            canvas.drawRoundRect(new RectF(-size/2+size/20,-size/2+size/20,size/2-size/20,size/2-size/20),size/10,size/10,paint);
            canvas.restore();
        }
        public boolean handleTap(float x,float y) {
            boolean condition = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2;
            return condition;
        }
        public void update(float factor) {
            scale = factor;
        }
    }
    private class YSTLine {
        private float l = 0;
        public void draw(Canvas canvas) {
            paint.setStrokeWidth(w/20);
            paint.setColor(Color.RED);
            canvas.drawLine(w/2-l,9*h/10,w/2,9*h/10,paint);
            canvas.drawLine(w/2,9*h/10,w/2+l,9*h/10,paint);
        }
        public void update(float factor) {
            l = w/2*factor;
        }
    }
    private class AnimationHandler extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener{
        private int dir = 0;
        private boolean isAnimating = false;
        private ValueAnimator startAnim = ValueAnimator.ofFloat(0,1),endAnim = ValueAnimator.ofFloat(1,0);
        public void onAnimationUpdate(ValueAnimator valueAnimator) {

            update((float) valueAnimator.getAnimatedValue());
        }
        public void onAnimationEnd(Animator animator) {
            if(isAnimating) {
                if(onSelectionChangeListener != null) {
                    if (dir == 0) {
                        onSelectionChangeListener.onSelect();
                    } else {
                        onSelectionChangeListener.onUnSelect();
                    }
                }
                dir = dir == 0?1:0;
                isAnimating = false;
            }
        }
        public AnimationHandler() {
            startAnim.setDuration(500);
            endAnim.setDuration(500);
            startAnim.addUpdateListener(this);
            endAnim.addUpdateListener(this);
            startAnim.addListener(this);
            endAnim.addListener(this);
        }
        public void start() {
            if(!isAnimating) {
                if (dir == 0) {
                    startAnim.start();
                } else {
                    endAnim.start();
                }
                isAnimating = true;
            }
        }
    }
}
