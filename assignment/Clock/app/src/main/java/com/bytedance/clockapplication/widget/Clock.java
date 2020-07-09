package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private float PANEL_RADIUS = 200.0f;// 表盘半径

    private float UNIT_DEGREE = (float) (6 * Math.PI / 180);// 一个小格的度数

    private int mWidth, mCenterX, mCenterY, mRadius;

    private int degreesColor;

    private Paint mNeedlePaint;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;
        PANEL_RADIUS = mRadius;

        drawDegrees(canvas);
        drawHoursValues(canvas);

        // todo 每一秒刷新一次，让指针动起来 OK
        //MainActivity里使用Handler刷新

    }

    //表盘
    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.03f);
        int longEnd = mCenterX - (int) (mWidth * 0.05f);
        int stopX;
        int stopY;

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0) {
                paint.setAlpha(CUSTOM_ALPHA);
                stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
                stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));
            } else {
                paint.setAlpha(FULL_ALPHA);
                stopX = (int) (mCenterX + longEnd * Math.cos(Math.toRadians(i)));
                stopY = (int) (mCenterX - longEnd * Math.sin(Math.toRadians(i)));
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));


            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * 表盘小时数字
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(degreesColor);
        paint.setTextSize(50f);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        int rText = mCenterX - (int) (mWidth * 0.10f);
        float biasY = (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;

        for (int i = 0; i < FULL_ANGLE; i += 30 /* Step */) {

            float startX = (float) (mCenterX + rText * Math.sin(Math.toRadians(i)));
            float startY = (float) (mCenterX - rText * Math.cos(Math.toRadians(i))) + biasY;

            if (i == 0) {
                canvas.drawText(String.valueOf(12), startX, startY, paint);
            } else {
                canvas.drawText(String.valueOf((i / 30)), startX, startY, paint);
            }

        }


    }

}