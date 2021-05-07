package org.gemini.wallpaper_changer;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.gemini.shared.Ints;

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
    debugLog("Received Uri " + uri.toString());
    List<String> candidates = new ArrayList<>();
    List<String> current;
    current = uri.getQueryParameters("file");
    if (current != null) candidates.addAll(current);
    current = uri.getQueryParameters("folder");
    if (current != null && !current.isEmpty()) {
      for (String s : current)
        scanFiles(new File(s), candidates);
    }
    debugLog("Totally " + candidates.size() + " files found.");

    Random random = new Random();
    while (candidates.size() > 0) {
      int index = random.nextInt(candidates.size());
      debugLog("Start to decode file " + candidates.get(index));
      Bitmap bmp = BitmapFactory.decodeFile(candidates.get(index));
      debugLog("Finished decoding file " + candidates.get(index));
      if (bmp == null) {
        candidates.remove(index);
        continue;
      }
      try {
        debugLog("Begin to set wallpaper to " + candidates.get(index));
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          result = manager.setBitmap(bmp,
                                     null,
                                     false,
                                     Ints.parseOr(uri.getQueryParameter("which"), 0));
        } else {
          manager.setBitmap(bmp);
          result = 1;
        }
        if (result == 0) {
          debugLog("Failed to set wallpaper to " + candidates.get(index));
          candidates.remove(index);
        } else {
          debugLog("Finished changing wallpaper to " + candidates.get(index));
          debugLog(candidates.size() + " candidates left.");
          break;
        }
      }
      catch (IOException ex) {
        Log.e(TAG, candidates.get(index) + ": " + ex);
        candidates.remove(index);
      }
    }
  }
}
