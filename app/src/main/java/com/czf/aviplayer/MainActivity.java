package com.czf.aviplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (Build.VERSION.SDK_INT >= 23) {
      requestPermissions(new String[]{
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    } else {
      onRealCreate();
    }
  }

  private void onRealCreate() {
    long avi = NativeLibInterface.openFile(Environment.getExternalStorageDirectory() + "/galleon.avi");
    if (avi != -1) {
      Log.d("------", "width: " + NativeLibInterface.frameWidth(avi) +
          ", height: " + NativeLibInterface.frameHeight(avi) + ", rate: " + NativeLibInterface.frameRate(avi));
      Bitmap bp = Bitmap.createBitmap(NativeLibInterface.frameWidth(avi), NativeLibInterface.frameHeight(avi), Bitmap.Config.RGB_565);
      NativeLibInterface.setFrame(avi, bp);
      ImageView iv = findViewById(R.id.iv);
      iv.setImageBitmap(bp);
      NativeLibInterface.closeFile(avi);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      onRealCreate();
    } else {
      Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
    }
  }
}
