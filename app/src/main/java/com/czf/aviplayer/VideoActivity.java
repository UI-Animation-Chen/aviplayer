package com.czf.aviplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

  private long avi = -1;

  private TextView tv;
  private int time = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video);

    tv = findViewById(R.id.tv);


    SurfaceView surfaceView = findViewById(R.id.surface_view);
    surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        openAviFile();
        if (avi != -1) {
          int intervalMs = (int)(1000/NativeLibInterface.frameRate(avi));
          Bitmap bp = Bitmap.createBitmap(NativeLibInterface.frameWidth(avi),
            NativeLibInterface.frameHeight(avi), Bitmap.Config.RGB_565);
          startRenderTask(holder.getSurface(), bp, intervalMs);
        } else {
          Toast.makeText(VideoActivity.this, "打开文件出错", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
        if (avi != -1) {
          NativeLibInterface.closeFile(avi);
          avi = -1;
        }
      }
    });
  }

  private void openAviFile() {
    avi = NativeLibInterface.openFile(Environment.getExternalStorageDirectory() + "/galleon.avi");
    if (avi != -1) {
      Log.d("------", "width: " + NativeLibInterface.frameWidth(avi) +
        ", height: " + NativeLibInterface.frameHeight(avi) +
        ", rate: " + NativeLibInterface.frameRate(avi));

    }
  }

  private void startRenderTask(final Surface surface, final Bitmap bp, final int intervalMs) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          if (avi == -1) break;
          renderFrame(surface, bp);
          if (tv != null) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                time += intervalMs;
                tv.setText(time + "");
              }
            });
          }
          SystemClock.sleep(intervalMs);
        }
      }
    }).start();
  }

  private void renderFrame(Surface surface, Bitmap bp) {
    long result = NativeLibInterface.setFrame(avi, bp);
    if (result != -1) {
      Canvas canvas = surface.lockCanvas(null);
      canvas.drawBitmap(bp, 0, 0, null);
      surface.unlockCanvasAndPost(canvas);
    }
  }

}
