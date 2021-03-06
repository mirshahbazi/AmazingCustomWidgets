package com.anwesome.games.imagecolorgrid;

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
 * Created by anweshmishra on 22/06/17.
 */

public class ImageColorGridView extends View {
    private int n = 3,time = 0,w,h;
    private int color = Color.parseColor("#0D47A1");
    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ConcurrentLinkedQueue<ColorGrid> colorGrids = new ConcurrentLinkedQueue<>();
    private AnimationHandler animationHandler;
    public ImageColorGridView(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            bitmap = Bitmap.createScaledBitmap(bitmap,w,w,true);
            for(int i=0;i<n*n;i++) {
                colorGrids.add(new ColorGrid(i));
            }
            animationHandler = new AnimationHandler();
        }
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap,w/2-bitmap.getWidth()/2,h/2-bitmap.getHeight()/2,paint);
        for(ColorGrid colorGrid:colorGrids) {
            colorGrid.draw(canvas);
        }
        time++;
        if(animationHandler != null) {
            animationHandler.animate();
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && animationHandler != null) {
            animationHandler.handleTap(event.getX(),event.getY());
        }
        return true;
    }
    private class ColorGrid {
        private float x,y,size,scale = 0,dir = 0,prevDir = -1;
        public void update() {
            scale+=dir*0.2f;
            if(scale > 1) {
                scale = 1;
                dir = 0;
                prevDir = 1;
            }
            if(scale < 0) {
                dir = 0;
                scale = 0;
                prevDir = -1;
            }
        }
        private void startUpdating() {
            dir = prevDir*-1;;
        }
        public boolean stopped() {
            return dir == 0;
        }
        public ColorGrid(int i) {
            this.size = w/n;
            this.x = size*(i%3)+size/2;
            this.y = (size*(i/3))+size/2;
        }
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.translate(x,y);
            int r = Color.red(Color.GRAY),g = Color.green(Color.GRAY),b = Color.blue(Color.GRAY);
            paint.setColor(Color.argb(150,r,g,b));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(new RectF(-size/2,-size/2,size/2,size/2),paint);
            canvas.save();
            canvas.scale(scale,scale);
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(150,r,g,b));
            canvas.drawRect(new RectF(-size/2,-size/2,size/2,size/2),paint);
            canvas.restore();
            canvas.restore();
        }
        public int hashCode() {
            return (int)(x+y);
        }
        public boolean handleTap(float x,float y) {
            boolean condition = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2 && dir == 0;
            if(condition) {
                startUpdating();
            }
            return condition;
        }
    }
    private class AnimationHandler {
        private boolean isAnimated = false;
        private ConcurrentLinkedQueue<ColorGrid> tappedGrids = new ConcurrentLinkedQueue<>();
        public void animate() {
            if(isAnimated) {
                for(ColorGrid colorGrid:colorGrids) {
                    colorGrid.update();
                    if(colorGrid.stopped()) {
                        tappedGrids.remove(colorGrid);
                        if(tappedGrids.size() == 0) {
                            isAnimated = false;
                        }
                    }
                    try {
                        Thread.sleep(5);
                        invalidate();
                    }
                    catch (Exception ex) {

                    }
                }
            }
        }
        public void handleTap(float x,float y) {
            for(ColorGrid colorGrid:colorGrids) {
                if(colorGrid.handleTap(x,y)) {
                    tappedGrids.add(colorGrid);
                    if(tappedGrids.size() == 1) {
                        isAnimated = true;
                        postInvalidate();
                    }
                }
            }
        }
    }
    public static void create(Activity activity,Bitmap bitmap) {
        ImageColorGridView imageColorGridView = new ImageColorGridView(activity,bitmap);
        Point size = DimensionsUtil.getDeviceDimension(activity);
        activity.addContentView(imageColorGridView,new ViewGroup.LayoutParams(size.x,size.x));
    }
}
