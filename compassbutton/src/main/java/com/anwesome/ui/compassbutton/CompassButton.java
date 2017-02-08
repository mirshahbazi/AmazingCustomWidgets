package com.anwesome.ui.compassbutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.*;

import com.anwesome.ui.dimensionsutil.DimensionsUtil;

/**
 * Created by anweshmishra on 09/02/17.
 */
public class CompassButton {
    private Activity activity;
    private int circleColor = Color.parseColor("#e53935"),triangleColor = Color.parseColor("#0097A7");
    private CompassButtonView compassButtonView;
    public CompassButton(Activity activity) {
        this.activity = activity;
    }
    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        if(compassButtonView!=null) {
            compassButtonView.invalidate();
        }
    }
    public void setTriangleColor(int triangleColor) {
        this.triangleColor = triangleColor;
        if(compassButtonView!=null) {
            compassButtonView.invalidate();
        }
    }
    public void show(int x,int y) {
        if(compassButtonView == null) {
            compassButtonView = new CompassButtonView(activity);
            Point size = DimensionsUtil.getDeviceDimension(activity);
            int w = size.x,h = size.y;
            activity.addContentView(compassButtonView,new ViewGroup.LayoutParams(w/4,w/4));
        }
        compassButtonView.setX(x);
        compassButtonView.setY(y);
    }
    private class CompassButtonView extends View {
        private boolean isAnimated = false;
        private float deg = 0,dir = 1;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        public CompassButtonView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            int w = canvas.getWidth(),h = canvas.getHeight(),r = w/2;
            if(h<w) {
                r = h/2;
            }
            canvas.drawCircle(w/2,h/2,r,paint);
            canvas.save();
            canvas.translate(w/2,h/2);
            canvas.rotate(deg);
            drawTriangle(canvas,paint,w);
            canvas.restore();
            if(!isAnimated) {
                deg+=dir*20;
                if(deg>=180) {
                    dir = -1;
                }
                if(deg<=0) {
                    dir = 0;
                }
                try {
                    Thread.sleep(50);
                    invalidate();
                } catch (Exception ex) {

                }
            }
        }
        public void drawTriangle(Canvas canvas,Paint paint,int w) {
            Path path = new Path();
            path.moveTo(w/4,0);
            path.lineTo(0,-w/4);
            path.lineTo(-w/4,0);
            path.lineTo(w/4,0);
            int l[] = {1,-1};
            for(int i=0;i<2;i++) {
                canvas.save();
                canvas.scale(1,l[i]);
                canvas.drawPath(path,paint);
                canvas.restore();
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN && !isAnimated && dir == 0) {
                dir = 1;
                isAnimated = true;
                postInvalidate();
            }
            return true;
        }
    }
}
