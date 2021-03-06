package com.anwesome.ui.circularcolorstack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.anwesome.ui.dimensionsutil.DimensionsUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by anweshmishra on 21/06/17.
 */

public class CircularColorStackView extends View {
    private int time = 0,w,h;
    private int colors[];
    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ColorStackContainer colorStackContainer;
    private AnimationHandler animationHandler;
    public CircularColorStackView(Context context,Bitmap bitmap,int colors[]) {
        super(context);
        this.colors = colors;
        this.bitmap = bitmap;
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            animationHandler = new AnimationHandler();
            colorStackContainer = new ColorStackContainer(colors);
            bitmap = Bitmap.createScaledBitmap(bitmap,2*w/3,2*w/3,true);
        }
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap,w/2-bitmap.getWidth()/2,h/2-bitmap.getHeight()/2,paint);
        if(colorStackContainer != null) {
            colorStackContainer.draw(canvas);
        }
        time++;
    }
    public void handleAnimationEnd() {
        if(colorStackContainer != null) {
            colorStackContainer.adjustParametersOnStop();
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && colorStackContainer != null) {
            colorStackContainer.startUpdating();
        }
        return true;
    }
    public void update(float factor) {
        if(colorStackContainer != null) {
            colorStackContainer.update(factor);
        }
        postInvalidate();
    }
    private class ColorStackContainer {
        private int index = 0,dir = 1;
        private ConcurrentLinkedQueue<ColorPie> colorPies = new ConcurrentLinkedQueue<>();
        public ColorStackContainer(int colors[]) {
            initColorPies(colors);
        }
        public void initColorPies(int[] colors) {
            if(colors.length > 0) {
                int gapDeg = 360/colors.length,deg = 0;
                for(int color:colors) {
                    colorPies.add(new ColorPie(color,gapDeg,deg));
                    deg+=gapDeg;
                }
            }
        }
        private ColorPie getColorPie() {
            ColorPie currPie = null;
            int i = 0;
            for(ColorPie colorPie:colorPies) {
                if(i == index) {
                    currPie = colorPie;
                    break;
                }
                i++;
            }
            return currPie;
        }
        public void update(float factor) {
            if(index < colorPies.size()) {
                ColorPie currPie = getColorPie();
                if(currPie != null) {
                    currPie.update(factor);
                }
            }
        }
        public void draw(Canvas canvas) {
            for(ColorPie colorPie:colorPies) {
                colorPie.draw(canvas);
            }
        }
        public void adjustParametersOnStop() {
            index+=dir;
            if(index < 0) {
                index = 0;
                dir = 1;
            }
            if(index == colorPies.size()) {
                index = colorPies.size()-1;
                dir = -1;
            }
        }
        public void startUpdating() {
            if(animationHandler != null) {
                if (dir == 1) {
                    animationHandler.open();
                } else {
                    animationHandler.close();
                }
            }}
        }
    private class ColorPie {
        private float deg = 0,maxDeg = 0,rot;
        private int color;
        public ColorPie(int color,float maxDeg,float rot) {
            this.maxDeg = maxDeg;
            this.color = color;
            this.rot = rot;
        }
        public void update(float factor) {
            deg = maxDeg*factor;
        }
        public void draw(Canvas canvas) {
            int r = Color.red(color),g = Color.green(color),b = Color.blue(color);
            paint.setColor(Color.argb(150,r,g,b));
            canvas.save();
            canvas.translate(w/2,h/2);
            canvas.rotate(rot);
            canvas.drawArc(new RectF(-w/3,-w/3,w/3,w/3),0,deg,true,paint);
            canvas.restore();
        }
        public int hashCode() {
            return (int)(deg+rot)+color;
        }
    }
    private class AnimationHandler extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        private ValueAnimator startAnim = ValueAnimator.ofFloat(0,1),endAnim = ValueAnimator.ofFloat(1,0);
        private boolean isAnimating = false;
        public AnimationHandler() {
            startAnim.setDuration(500);
            endAnim.setDuration(500);
            startAnim.addUpdateListener(this);
            endAnim.addUpdateListener(this);
            startAnim.addListener(this);
            endAnim.addListener(this);
        }
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float factor = (float)valueAnimator.getAnimatedValue();
            update(factor);
        }
        public void onAnimationEnd(Animator animator) {
            if(isAnimating) {
                handleAnimationEnd();
                isAnimating = false;
            }
        }
        public void open() {
            if(!isAnimating) {
                startAnim.start();
                isAnimating = true;
            }
        }
        public void close() {
            if(!isAnimating) {
                endAnim.start();
                isAnimating = true;
            }
        }
    }
    public static void create(Activity activity, Bitmap bitmap, int colors[]) {
        CircularColorStackView view = new CircularColorStackView(activity,bitmap,colors);
        Point size = DimensionsUtil.getDeviceDimension(activity);
        activity.addContentView(view,new ViewGroup.LayoutParams(size.x,size.x));
    }
}
