package org.gemini.wallpaper_changer;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WallpaperChangerActivity extends WallpaperActivity
{
  private static final String TAG =
      WallpaperChangerActivity.class.getSimpleName();

  private static void scanFiles(File p, List<String> candidates)
  {
    if (p.isFile())
      candidates.add(p.getAbsolutePath());
    else if (p.isDirectory())
    {
      File[] files = p.listFiles();
      if (files != null && files.length > 0)
        for (File s : files) scanFiles(s, candidates);
    }
  }

  // Uri format = ?file={file} or ?folder={folder}
  @Override
  protected void processUri(WallpaperManager manager, Uri uri)
  {
    Log.i(TAG, "Received Uri " + uri.toString());
    List<String> candidates = new ArrayList<>();
    List<String> current;
    current = uri.getQueryParameters("file");
    if (current != null) candidates.addAll(current);
    current = uri.getQueryParameters("folder");
    if (current != null && !current.isEmpty())
    {
      for (String s : current)
        scanFiles(new File(s), candidates);
    }

    Random random = new Random();
    while (candidates.size() > 0)
    {
      int index = random.nextInt(candidates.size());
      Bitmap bmp = BitmapFactory.decodeFile(candidates.get(index));
      if (bmp != null)
      {
        try
        {
          manager.setBitmap(bmp);
          break;
        }
        catch (IOException ex) { candidates.remove(index); }
      }
      else candidates.remove(index);
    }
  }
}
