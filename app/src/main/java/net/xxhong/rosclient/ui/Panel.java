package net.xxhong.rosclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {
    private CanvasThread canvasThread;
    public Bitmap bmpIcon;

    public Panel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Panel(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (canvasThread == null) {
            canvasThread = new CanvasThread(getHolder(), this);
            canvasThread.getSurfaceHolder().addCallback(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (bmpIcon != null) {
            canvas.drawBitmap(bmpIcon,
                    0, 0, null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init();
        if (canvasThread != null && !canvasThread.isAlive()) {
            canvasThread = new CanvasThread(getHolder(), this);
            canvasThread.setRunning(true);
            canvasThread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        if (canvasThread != null && canvasThread.isAlive()) {
            canvasThread.setRunning(false);
            while(retry) {
                try {
                    canvasThread.join();
                    retry = false;
                } catch(InterruptedException ie) {
                    //Try again and again and again
                }
                break;
            }
        }
        canvasThread = null;
        bmpIcon.recycle();
        bmpIcon = null;
    }

}