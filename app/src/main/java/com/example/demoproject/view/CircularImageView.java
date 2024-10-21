package com.example.demoproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.example.demoproject.R;

public class CircularImageView extends AppCompatImageView {

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context,
                             AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getColor());

        canvas.drawCircle((float) width / 2, (float) height / 2, radius, paint);

        super.onDraw(canvas); // Draw the image on top of the circle
    }

    public int getColor() {
        return ContextCompat.getColor(getContext(), R.color.white);
    }
}
