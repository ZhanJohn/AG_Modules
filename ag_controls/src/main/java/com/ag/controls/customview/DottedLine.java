package com.ag.controls.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DottedLine extends View {

    public DottedLine(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        Path path = new Path();
        path.moveTo(0, 10);
        path.lineTo(480, 10);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);


//        paint.setStrokeWidth((float) 1.0);              //设置线宽
//        canvas.drawLine(50, 50, 450, 50, paint);
    }
}