package com.movecirclesversion2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyView extends View implements View.OnTouchListener {
    public final int RADIUS = 60;
    //Refactor: Think if Map can be replaced with simpler DS.
    public Map<Integer, MyCircle> fingers = new HashMap<>();
    public List<Paint> paints = new ArrayList<>();
    public int actionIndex;
    public boolean circleIsUnder;


    public MyView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    protected void onDraw(Canvas canvas) {
        if (fingers.size() != 0) {
            for (Integer key : fingers.keySet()) {
                MyCircle circle = fingers.get(key);
                if (circle != null) {
                    canvas.drawCircle(circle.getX(), circle.getY(), RADIUS, paints.get(key));
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int actionMasked = event.getActionMasked();
        int pointerCount = event.getPointerCount();
        actionIndex = event.getActionIndex();

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Motion Event", "ACTION_DOWN");
                float cordX = event.getX(actionIndex);
                float cordY = event.getY(actionIndex);
                MyCircle circleFirst = new MyCircle();
                circleFirst.setX(cordX);
                circleFirst.setY(cordY);
                removeIfUnder(circleFirst);
                if (actionIndex == 0 && !circleIsUnder) {
                    fingers.clear();
                    paints.clear();
                    circleIsUnder = false;
                }
                System.out.println("Action Index from DOWN: " + actionIndex);
                fingers.put(actionIndex, circleFirst);
                addColor();
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("Motion Event", "ACTION_POINTER_DOWN");
                MyCircle moreCircles = new MyCircle();
                moreCircles.setX(event.getX(actionIndex));
                moreCircles.setY(event.getY(actionIndex));
                System.out.println("Action Index from POINTER DOWN: " + actionIndex);

                fingers.put(actionIndex, moreCircles);
                addColor();
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("Motion Event", "ACTION_MOVE");
                for (int i = 0; i < pointerCount; i++) {
                    MyCircle circle = new MyCircle();
                    circle.setX(event.getX(i));
                    circle.setY(event.getY(i));
                    fingers.put(i, circle);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.d("Motion Event", "ACTION_UP");
                break;

            case MotionEvent.ACTION_POINTER_UP:
                Log.d("Motion Event", "ACTION_POINTER_UP");
                break;

        }
        return true;
    }

    public void addColor() {
        Paint paint = new Paint();
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        paint.setColor(color);
        paints.add(paint);
    }

    public void removeIfUnder(MyCircle circle) {
        for (Integer key : fingers.keySet()) {
            MyCircle circleUnder = fingers.get(key);
            System.out.println(fingers.toString());
            float centerX = circleUnder.getX();
            float centerY = circleUnder.getY();
            if ((Math.pow(circle.getX() - centerX, 2) + Math.pow(circle.getY() - centerY, 2) < Math.pow(RADIUS, 2))) {
                circleIsUnder = true;
                fingers.remove(key);
                return;
            }
            else
            {
                circleIsUnder = false;
            }
        }
    }


    public class MyCircle {
        public float x;
        public float y;


        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}