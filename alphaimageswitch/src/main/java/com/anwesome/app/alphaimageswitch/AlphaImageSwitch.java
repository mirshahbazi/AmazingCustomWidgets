package com.anwesome.app.alphaimageswitch;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

import java.util.*;

/**
 * Created by anweshmishra on 24/02/17.
 */
public class AlphaImageSwitch {
    private Activity activity;
    private List<AlphaImageSwitchButton> buttons = new ArrayList<>();
    public AlphaImageSwitch(Activity activity) {
        this.activity = activity;
    }
    public void addImageSwitchButton(AlphaImageSwitchButton imageSwitchButton) {
        this.buttons.add(imageSwitchButton);
    }
    public void show() {

    }
    private class AlphaImageSwitchView extends View {
        private boolean isAnimated = false;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private AlphaImageSwitchButton currButton=null,prevButton = null;
        public AlphaImageSwitchView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            for(AlphaImageSwitchButton imageSwitchButton:buttons) {
                imageSwitchButton.draw(canvas,paint);
            }
            if(isAnimated) {
                if(currButton!=null) {
                    currButton.update();
                }
                if(prevButton!=null) {
                    prevButton.update();
                }
                boolean currButtonStopped = (currButton!=null && currButton.isStopped());
                if((prevButton == null && currButtonStopped) || (prevButton!=null && prevButton.isStopped() && currButtonStopped)) {
                    prevButton.unselect();
                    prevButton = currButton;
                    currButton = null;
                }
                try {
                    Thread.sleep(50);
                    invalidate();
                }
                catch(Exception ex) {

                }
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

    }
}
