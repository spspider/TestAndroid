package com.example.facedetector.Helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Color;
import android.graphics.RectF;

public class RectOverlay extends  GraphicOverlay.Graphic {
    private int RectColor = Color.GREEN;
    private float StrokeWidth = 4.9f;
    private Paint RectPaint;
    private GraphicOverlay graphicOverlay;
    private  Rect rect;

    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);
        RectPaint= new Paint();
        RectPaint.setColor(RectColor);
        RectPaint.setStyle(Paint.Style.STROKE);
        RectPaint.setStrokeWidth(StrokeWidth);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;
         postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
            RectF rectF = new RectF(rect);
            rectF.left= translateX((rectF.left));
        rectF.right= translateX((rectF.right));
        rectF.top= translateX((rectF.top));
        rectF.bottom= translateX((rectF.bottom));

        canvas.drawRect(rectF, RectPaint);


    }
}
