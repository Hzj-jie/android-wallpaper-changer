package org.gemini.wallpaper_changer;

import android.app.WallpaperManager;
import android.net.Uri;

import java.io.IOException;

public final class ClearWallpaperActivity extends WallpaperActivity
{
  private static final String TAG =
      ClearWallpaperActivity.class.getSimpleName();

  @Override
  protected void processUri(WallpaperManager manager, Uri uri)
  {
    debugLog(TAG, "Received Uri " + uri.toString());
    try { manager.clear(); }
    catch (IOException ex) {}
  }
}
