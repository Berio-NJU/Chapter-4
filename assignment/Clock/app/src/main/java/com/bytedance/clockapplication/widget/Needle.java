package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * 另写一个指针View控件
 */
public class Needle extends View {
    private final static String TAG = Needle.class.getSimpleName();

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;


    private float PANEL_RADIUS = 200.0f;// 表盘半径

    private float HOUR_POINTER_LENGTH;// 指针长度
    private float MINUTE_POINTER_LENGTH;
    private float SECOND_POINTER_LENGTH;
    private float UNIT_DEGREE = (float) (6 * Math.PI / 180);// 一个小格的度数

    private int mWidth, mCenterX, mCenterY, mRadius;

    private Paint mNeedlePaint;


    public Needle(Context context) {
        super(context);
        init(context,null);
    }

    public Needle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Needle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;
        PANEL_RADIUS = mRadius;
        HOUR_POINTER_LENGTH = PANEL_RADIUS - 400;
        MINUTE_POINTER_LENGTH = PANEL_RADIUS - 250;
        SECOND_POINTER_LENGTH = PANEL_RADIUS - 150;

        drawNeedles(canvas);

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int nowSeconds = now.getSeconds();
        // 画秒针
        drawPointer(canvas, 2, nowSeconds);
        // 画分针
        // todo 画分针 OK
        drawPointer(canvas, 1, nowMinutes);
        // 画时针
        int part = nowMinutes / 12;
        drawPointer(canvas, 0, 5 * nowHours + part);


    }

    private void drawPointer(Canvas canvas, int pointerType, int value) {

        float degree;
        float[] pointerHeadXY = new float[2];

        mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        switch (pointerType) {
            case 0:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
                break;
            case 1:
                // todo 画分针，设置分针的颜色 OK
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.BLUE);
                pointerHeadXY = getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
                break;
            case 2:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.GREEN);
                pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
                break;
        }


        canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
        xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
        return xy;
    }
}
