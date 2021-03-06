package com.unistrong.baselibs.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.unistrong.baselibs.utils.DensityUtils;


/**
 * Created by Nipun
 */

public class BoxLoaderView extends View {

    private static final int FRAME_RATE = 2;
    private static final int DEFAULT_STROKE_WIDTH = 20;
    private static final int DEFAULT_STROKE_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_LOADER_COLOR = 0xff3D80DB;

    private int speed;
    private int strokeWidth;
    private int strokeColor, loaderColor;
    private Box box, outBox;

    private boolean onAnimating = true;


    public BoxLoaderView(Context context) {
        this(context, null);
    }

    public BoxLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        strokeColor = DEFAULT_STROKE_COLOR;
        loaderColor = DEFAULT_LOADER_COLOR;
        strokeWidth = DEFAULT_STROKE_WIDTH;
        speed = DensityUtils.dp2px(getContext(), 5);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        strokeWidth = (int) ((right - left) * .35f);
        if (outBox == null) {
            outBox = new Box(left, top, right, bottom, strokeColor, 10);
            outBox.getPaint().setStrokeWidth(strokeWidth);
        }
        if (box == null) {
            box = new Box(left + strokeWidth, top + strokeWidth, right / 2 - strokeWidth, bottom / 2 - strokeWidth, loaderColor, 10);
            box.setDx(speed);
            box.setDy(speed);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(outBox.getLeft(), outBox.getTop(), outBox.getRight(), outBox.getBottom(), outBox.getPaint());
        boolean dirChange = box.bounce(canvas, strokeWidth);
        rectifyBoundaries(canvas, box);
        canvas.drawRect(box.getLeft(), box.getTop(), box.getRight(), box.getBottom(), box.getPaint());
        int delayMillis = dirChange ? FRAME_RATE * 20 : FRAME_RATE;
        if (onAnimating) {
            postInvalidateDelayed(delayMillis);
        }
    }

    private void rectifyBoundaries(Canvas canvas, Box box) {
        if (box.getLeft() < strokeWidth) {
            box.getrect().left = strokeWidth;
        }
        if (box.getTop() < strokeWidth) {
            box.getrect().top = strokeWidth;
        }
        if (box.getRight() > canvas.getWidth() - strokeWidth) {
            box.getrect().right = canvas.getWidth() - strokeWidth;
        }
        if (box.getBottom() > canvas.getHeight() - strokeWidth) {
            box.getrect().bottom = canvas.getHeight() - strokeWidth;
        }
    }

    public void startAnimation() {
        onAnimating = true;
        postInvalidate();
    }

    public void cancelAnimation() {
        onAnimating = false;
    }

    private static class Box {
        private int c, r, dx, dy, dir;
        private Rect rect;
        private Paint paint;

        //dir : 0 : right, 1 : down, 2 : left, 3 : up

        private Box(int left, int top, int right, int bottom, int color, int radius) {
            rect = new Rect(left, top, right, bottom);
            c = color;
            r = radius;
            paint = new Paint();
            paint.setColor(c);
            dx = 0;
            dy = 0;
            dir = 0;
        }

        public void setColor(int col) {
            c = col;
        }


        public void setDx(int speed) {
            dx = speed;
        }

        public void setDy(int speed) {
            dy = speed;
        }

        public int getLeft() {
            return rect.left;
        }

        public int getTop() {
            return rect.top;
        }

        public int getRight() {
            return rect.right;
        }

        public int getBottom() {
            return rect.bottom;
        }

        public int getRadius() {
            return r;
        }

        public Paint getPaint() {
            return paint;
        }

        public void increaseRight() {
            rect.right += dx;
        }

        public void decreaseRight() {
            rect.right -= dx;
        }

        public void increaseLeft() {
            rect.left += dx;
        }

        public void decreaseLeft() {
            rect.left -= dx;
        }

        public void increaseTop() {
            rect.top += dy;
        }

        public void decreaseTop() {
            rect.top -= dy;
        }

        public void increaseBottom() {
            rect.bottom += dy;
        }

        public void decreaseBottom() {
            rect.bottom -= dy;
        }

        public Rect getrect() {
            return rect;
        }

        //Bounce of edge
        public boolean bounce(Canvas canvas, int strokeWidth) {
            switch (dir) {
                case 0:
                    if (rect.right < canvas.getWidth() - strokeWidth) {
                        increaseRight();
                    } else {
                        increaseLeft();
                        if (rect.left > canvas.getWidth() / 2) {
                            dir++;
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (rect.bottom < canvas.getHeight() - strokeWidth) {
                        increaseBottom();
                    } else {
                        increaseTop();
                        if (rect.top > canvas.getHeight() / 2) {
                            dir++;
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (rect.left > strokeWidth) {
                        decreaseLeft();
                    } else {
                        decreaseRight();
                        if (rect.right < canvas.getWidth() / 2) {
                            dir++;
                            return true;
                        }
                    }
                    break;
                case 3:
                    if (rect.top > strokeWidth) {
                        decreaseTop();
                    } else {
                        decreaseBottom();
                        if (rect.bottom < canvas.getHeight() / 2) {
                            dir = 0;
                            return true;
                        }
                    }
                    break;
            }
            return false;
        }
    }

}
