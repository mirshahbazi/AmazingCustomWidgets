package com.anwesome.games.crosslineview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.anwesome.ui.dimensionsutil.DimensionsUtil;

/**
 * Created by anweshmishra on 14/07/17.
 */

public class CrossLineView extends View {
    private int w,h,time = 0;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ViewRenderingController viewRenderingController;
    private CrossLineView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            paint.setColor(Color.parseColor("#673ab7"));
            paint.setStrokeWidth(5);
            viewRenderingController = new ViewRenderingController();
        }
        viewRenderingController.render(canvas);
        time++;
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            viewRenderingController.handleTap(event.getX(),event.getY());
        }
        return true;
    }
    private class CrossLine {
        public void draw(Canvas canvas,float scale) {
            canvas.save();
            canvas.translate(w/2,h/2);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(new RectF(-w/10,-w/10,w/10,w/10),0,360*scale,true,paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(0,0,w/10,paint);
            for(int i=0;i<4;i++) {
                canvas.save();
                canvas.rotate(i*90+45);
                canvas.drawLine(0,0,w/4*scale,0,paint);
                canvas.restore();
            }
            canvas.restore();
        }
        public boolean handleTap(float x,float y) {
            return x>=w/2 -w/10 && x<=w/2+w/10 && y>=h/2-w/10 && y<=h/2+w/10;
        }
    }
    public class StateContainer {
        private float scale = 0,dir = 0;
        public void update() {
            scale+=0.15f*dir;
            if(scale >= 1) {
                dir = 0;
                scale = 1;
            }
            if(scale <= 0) {
                dir = 0;
                scale = 0;
            }
            if(dir == 0 && onCrossSelectionListener!=null) {
                if(scale >= 1) {
                    onCrossSelectionListener.onCrossSelected();
                }
                if(scale <= 0) {
                    onCrossSelectionListener.onCrossUnSelected();
                }
            }
        }
        public void startUpdating() {
            dir = scale <= 0?1:-1;
        }
        public boolean stopped() {
            return dir == 0;
        }
    }
    private class ViewRenderingController {
        private StateContainer stateContainer = new StateContainer();
        private CrossLine crossLine = new CrossLine();
        private boolean animated = false;
        public void render(Canvas canvas) {
            crossLine.draw(canvas,stateContainer.scale);
            if(animated) {
                stateContainer.update();
                if(stateContainer.stopped()) {
                    animated = false;
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
            if(crossLine.handleTap(x,y) && !animated) {
                stateContainer.startUpdating();
                animated = true;
                postInvalidate();
            }
        }
    }
    private OnCrossSelectionListener onCrossSelectionListener;
    public void setOnCrossSelectionListener(OnCrossSelectionListener onCrossSelectionListener) {
        this.onCrossSelectionListener = onCrossSelectionListener;
    }
    public static void create(Activity activity,OnCrossSelectionListener...listeners) {
        CrossLineView crossLineView = new CrossLineView(activity);
        Point size = DimensionsUtil.getDeviceDimension(activity);
        if(listeners.length == 1) {
            crossLineView.setOnCrossSelectionListener(listeners[0]);
        }
        activity.addContentView(crossLineView,new ViewGroup.LayoutParams(size.x,size.x));
    }
    public interface OnCrossSelectionListener {
        void onCrossSelected();
        void onCrossUnSelected();
    }
}
