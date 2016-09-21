package org.gemini.wallpaper_changer;

import android.app.WallpaperManager;
import android.net.Uri;
import android.os.Build;

public final class WallpaperOffsetsActivity extends WallpaperActivity
{
  private static final String TAG =
      WallpaperOffsetsActivity.class.getSimpleName();

  private static float parseQuery(Uri uri, String param)
  {
    String s = uri.getQueryParameter(param);
    if (s != null)
    {
      try { return Float.parseFloat(s); }
      catch (NumberFormatException ex) { return 0; }
    }
    return 0;
  }

  // Uri format = ?x={x}&y={y}
  @Override
  protected void processUri(WallpaperManager manager, Uri uri)
  {
    debugLog(TAG, "Received Uri " + uri.toString());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1)
    {
      float x = parseQuery(uri, "x");
      float y = parseQuery(uri, "y");
      if (x >= 0 && y >= 0 && (x > 0 || y > 0))
      {
        debugLog(TAG, "Offsets x " + x + ", y " + y);
        manager.setWallpaperOffsetSteps(x, y);
      }
    }
  }
}
