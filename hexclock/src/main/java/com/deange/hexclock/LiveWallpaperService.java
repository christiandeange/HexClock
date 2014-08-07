package com.deange.hexclock;

import android.graphics.Canvas;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new HexClockWallpaperEngine();
    }

    private class HexClockWallpaperEngine extends Engine implements SecondlyTimer.OnSecondListener {

        private final SecondlyTimer mTimer;
        private final Runnable mDrawRunnable = new Runnable() {
            @Override
            public void run() {
                draw(Instant.get());
            }
        };

        public HexClockWallpaperEngine() {
            mTimer = new SecondlyTimer(this);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mTimer.start();
            } else {
                mTimer.stop();
            }
        }

        @Override
        public void onSurfaceCreated(final SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mTimer.post(mDrawRunnable);
            mTimer.start();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mTimer.stop();
        }

        private void draw(final Instant instant) {
            Canvas canvas = null;
            try {
                canvas = getSurfaceHolder().lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(HexAnimator.convertInstantToColour(instant));
                }
            } finally {
                if (canvas != null) {
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public void onUpdate(final Instant instant) {
            draw(instant);
        }
    }

}
