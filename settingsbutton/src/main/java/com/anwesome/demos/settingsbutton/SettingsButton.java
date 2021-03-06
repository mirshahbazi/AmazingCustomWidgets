package com.anwesome.demos.settingsbutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.anwesome.ui.dimensionsutil.DimensionsUtil;

/**
 * Created by anweshmishra on 25/03/17.
 */
public class SettingsButton {
    private Activity activity;
    private SettingsButtonView settingsButtonView;
    private View.OnClickListener onClickListener;
    private SettingsButtonController settingsButtonController;
    public SettingsButton(Activity activity) {
        this.activity = activity;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void show(int x,int y) {
        if(settingsButtonView == null) {
            settingsButtonView = new SettingsButtonView(activity);
            Point size = DimensionsUtil.getDeviceDimension(activity);
            int w = size.x,h = size.y;
            settingsButtonController = SettingsButtonController.getInstance(w/4,w/4,w/2);
            activity.addContentView(settingsButtonView,new ViewGroup.LayoutParams(w/2,w/2));
        }
        settingsButtonView.setX(x);
        settingsButtonView.setY(y);
    }
    private class SettingsButtonView extends View {
        private boolean isAnimated = false;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        public SettingsButtonView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            settingsButtonController.draw(canvas,paint);
            if(isAnimated) {
                settingsButtonController.update();
                if(settingsButtonController.stopped()) {
                    isAnimated = false;
                    if(onClickListener!=null) {
                        onClickListener.onClick(this);
                    }
                }
                try{
                    Thread.sleep(100);
                    invalidate();
                }
                catch (Exception ex) {

                }
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN && !isAnimated) {
                settingsButtonController.startMoving();
                isAnimated = true;
                postInvalidate();
            }
            return true;
        }
    }
}
