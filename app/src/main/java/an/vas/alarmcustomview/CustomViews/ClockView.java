package an.vas.alarmcustomview.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

import an.vas.alarmcustomview.R;

public class ClockView extends View {
    private static final String TAG = "ClockView";

    private Paint paint;

    private int circleWidth = 35;
    private int radius;
    private int pointRadius;

    private int curSeconds, curMinutes, curHours;
    private int secHandWidth, minHandWidth, hourHandWidth;
    private int secHandLen, minHandLen, hourHandLen;

    // background
    private int backgroundColor;
    private int clockHandsColor;
    private int numbersColor;
    private int shadowColor;

    // allocated vars to use in draw method
    private float x, y, indent;
    private float xFinish, yFinish;
    private float xStart, yStart;
    private float xCenter, yCenter;
    private double d;
    private double sec, min, hour;
    Date currentTime;


    public ClockView(Context context)
    {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        Init();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        currentTime = Calendar.getInstance().getTime();
        curHours = array.getInteger(R.styleable.ClockView_hour, currentTime.getHours());
        curHours %= 12;
        curMinutes = array.getInteger(R.styleable.ClockView_min, currentTime.getMinutes());
        curMinutes %= 60;
        curSeconds = array.getInteger(R.styleable.ClockView_sec,currentTime.getSeconds());
        curSeconds %= 60;

        hourHandWidth = array.getInteger(R.styleable.ClockView_hour_hand_width,15);
        minHandWidth = array.getInteger(R.styleable.ClockView_min_hand_width,10);
        secHandWidth = array.getInteger(R.styleable.ClockView_sec_hand_width,6);

        backgroundColor = array.getColor(R.styleable.ClockView_background_color, getResources().getColor(R.color.white));
        clockHandsColor = array.getColor(R.styleable.ClockView_clock_hands_color, getResources().getColor(R.color.black));
        numbersColor = array.getColor(R.styleable.ClockView_numbers_color, getResources().getColor(R.color.black));
        shadowColor = array.getColor(R.styleable.ClockView_shadow_color, getResources().getColor(R.color.gray));
        array.recycle();

    }


    public int getCurSeconds() {
        return curSeconds;
    }

    public void setCurSeconds(int curSeconds) {
        this.curSeconds = curSeconds % 60;
    }

    public int getCurMinutes() {
        return curMinutes;
    }

    public void setCurMinutes(int curMinutes) {
        this.curMinutes = curMinutes % 60;
    }

    public int getCurHours() {
        return curHours;
    }

    public void setCurHours(int curHours) {
        this.curHours = curHours % 12;
    }

    public int getSecHandWidth() {
        return secHandWidth;
    }

    public void setSecHandWidth(int value) {
        this.secHandWidth = value;
    }
    public int getMinHandWidth() {
        return minHandWidth;
    }

    public void setMinHandWidth(int value) {
        this.minHandWidth = value;
    }

    public int getHourHandWidth() {
        return hourHandWidth;
    }

    public void setHourHandWidth(int value) {
        this.hourHandWidth = value;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int value) {
        this.shadowColor = value;
    }

    public int getNumbersColor() {
        return numbersColor;
    }

    public void setNumbersColor(int value) {
        this.numbersColor = value;
    }

    public int getClockHandsColor() {
        return clockHandsColor;
    }

    public void setClockHandsColor(int value) {
        this.clockHandsColor = value;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int value) {
        this.backgroundColor = value;
    }

    private void Init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

        new Thread(){
            @Override
            public void run() {
                while (true){
                    recalculateTime();
                    postInvalidate();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();

        int realWidth = resolveSize(height, height + topPadding + bottomPadding);
        int realHeight = resolveSize(width, width + leftPadding + rightPadding);

        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        int w = getWidth() - getPaddingLeft() - getPaddingRight() - circleWidth;
        int h = getHeight() - getPaddingTop() - getPaddingBottom() - circleWidth;
        radius = Math.min(w, h) / 2 ;
        circleWidth = radius / 10 + 1;
        pointRadius = circleWidth / 8 + 1;

        secHandLen = (int) ((float)(radius) * 9 / 12);
        minHandLen = (int) ((float)(radius) * 8 / 12);
        hourHandLen = (int) ((float)(radius) * 5 / 12);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int center = Math.min(getWidth(), getHeight()) / 2;
        xCenter = center;
        yCenter = center;

        drawCircleAroundСlockFace(center, canvas);
        drawBackground(center, canvas);
        drawPointsAroundTheClock(xCenter, yCenter, canvas);


        sec = (curSeconds * 360 * Math.PI) / (60 * 180);
        min = (curMinutes * 360 * Math.PI) / (60 * 180);
        hour = (curHours * 360 * Math.PI) / (12 * 180) + (curMinutes * 30 * Math.PI) / (180 * 60);

        // Shadows
        drawClockHand(xCenter + 10, yCenter + 10, sec, shadowColor, secHandWidth, secHandLen, canvas);
        drawClockHand(xCenter + 7, yCenter + 7, min, shadowColor, minHandWidth, minHandLen, canvas);
        drawClockHand(xCenter + 6, yCenter + 6, hour, shadowColor, hourHandWidth, hourHandLen, canvas);

        // Clock hands
        drawClockHand(xCenter, yCenter, sec, clockHandsColor, secHandWidth, secHandLen, canvas);
        drawClockHand(xCenter, yCenter, min, clockHandsColor, minHandWidth, minHandLen, canvas);
        drawClockHand(xCenter, yCenter, hour, clockHandsColor, hourHandWidth, hourHandLen, canvas);

        drawClockNumbers(xCenter, yCenter, canvas);
    }

    private void recalculateTime ()
    {
        curSeconds++;


        if (curSeconds == 60){
            curSeconds = 0;
            currentTime = Calendar.getInstance().getTime();
            curHours = currentTime.getHours();
            curHours %= 12;
            curMinutes = currentTime.getMinutes();
            curMinutes %= 60;
            curSeconds = currentTime.getSeconds();
            curSeconds %= 60;
            if (curMinutes == 60){
                curMinutes = 0;
                curHours += 1;
            }
        }
    }

    private int calculateRadiusOfPoint (int i)
    {
        if (i % 5 == 0){
            return pointRadius*2;
        } else {
            return pointRadius;
        }
    }

    private void drawPointsAroundTheClock(float xCenter, float yCenter, Canvas canvas)
    {
        paint.setColor(clockHandsColor);
        for (int i = 0; i < 60; i++) {
            d = (i * 360 * Math.PI) / (180 * 60);
            x = (float) ((radius- circleWidth -1) * Math.sin(d)) + xCenter;
            y = -(float) ((radius- circleWidth -1) * Math.cos(d)) + yCenter;
            canvas.drawCircle(x - 1,y -1 ,calculateRadiusOfPoint(i), paint);
        }
    }


    private void drawClockHand(float xCenter, float yCenter, double value, int color, int width, int len, Canvas canvas)
    {
        paint.setColor(color);
        paint.setStrokeWidth(width);
        xFinish = (float) (len * Math.sin(value)) + xCenter;
        yFinish = - (float) (len * Math.cos(value)) + yCenter;
        xStart = (float) - (len * Math.sin(value) * 0.2) + xCenter;
        yStart = - (float) - (len * Math.cos(value) * 0.2) + yCenter;
        canvas.drawLine(xStart, yStart, xFinish, yFinish, paint);
    }

    private void drawBackground( int center, Canvas canvas)
    {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);
        canvas.drawCircle(center,center,radius - circleWidth / 2, paint);
    }

    private void drawCircleAroundСlockFace( int center, Canvas canvas)
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleWidth);
        paint.setColor(clockHandsColor);
        canvas.drawCircle(center,center,radius - circleWidth / 2, paint);
    }

    private void drawClockNumbers(float xCenter, float yCenter, Canvas canvas)
    {
        paint.setColor(numbersColor);
        paint.setStrokeWidth(radius/60);
        paint.setTextSize((float) (radius / 3.5));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        indent = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;


        for (int i = 0; i < 12; i++) {
            d = (i+1) * 30 * Math.PI / 180;
            x = (float) ((radius - circleWidth * 3.5) * Math.sin(d) + xCenter);
            y = (float) (-(radius - circleWidth * 3.5) * Math.cos(d) + yCenter) + indent;

            canvas.drawText(String.valueOf(i+1), x, y, paint);
        }
    }

}