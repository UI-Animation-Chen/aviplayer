package com.czf.aviplayer;

import android.graphics.Bitmap;

public class NativeLibInterface {

  static {
    System.loadLibrary("native-lib");
  }

  public static native long openFile(String filePath);

  public static native void closeFile(long fileFd);

  public static native int frameWidth(long fileFd);

  public static native int frameHeight(long fileFd);

  public static native double frameRate(long fileFd);

  public static native long setFrame(long fileFd, Bitmap bp);

}
