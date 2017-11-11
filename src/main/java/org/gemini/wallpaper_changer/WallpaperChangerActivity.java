package org.gemini.wallpaper_changer;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WallpaperChangerActivity extends WallpaperActivity {
  private static final String TAG =
      WallpaperChangerActivity.class.getSimpleName();

  private static void scanFiles(File p, List<String> candidates) {
    if (p.isFile()) {
      candidates.add(p.getAbsolutePath());
    } else if (p.isDirectory()) {
      File[] files = p.listFiles();
      if (files != null && files.length > 0)
        for (File s : files) scanFiles(s, candidates);
    }
  }

  // Uri format = ?file={file} or ?folder={folder}
  @Override
  protected void processUri(WallpaperManager manager, Uri uri) {
    debugLog(TAG, "Received Uri " + uri.toString());
    List<String> candidates = new ArrayList<>();
    List<String> current;
    current = uri.getQueryParameters("file");
    if (current != null) candidates.addAll(current);
    current = uri.getQueryParameters("folder");
    if (current != null && !current.isEmpty()) {
      for (String s : current)
        scanFiles(new File(s), candidates);
    }
    debugLog(TAG, "Totally " + candidates.size() + " files found.");

    Random random = new Random();
    while (candidates.size() > 0) {
      int index = random.nextInt(candidates.size());
      debugLog(TAG, "Start to decode file " + candidates.get(index));
      Bitmap bmp = BitmapFactory.decodeFile(candidates.get(index));
      debugLog(TAG, "Finished decoding file " + candidates.get(index));
      if (bmp != null) {
        try {
          debugLog(TAG, "Begin to set wallpaper to " + candidates.get(index));
          int result = 0;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = manager.setBitmap(bmp, null, false);
          } else {
            manager.setBitmap(bmp);
            result = 1;
          }
          if (result == 0) {
            debugLog(TAG, "Failed to set wallpaper to " +
                          candidates.get(index));
            candidates.remove(index);
          } else {
            debugLog(TAG,
                     "Finished changing wallpaper to " + candidates.get(index));
            debugLog(TAG, candidates.size() + " candidates left.");
            break;
          }
        }
        catch (IOException ex) { candidates.remove(index); }
      } else {
        candidates.remove(index);
      }
    }
  }
}
