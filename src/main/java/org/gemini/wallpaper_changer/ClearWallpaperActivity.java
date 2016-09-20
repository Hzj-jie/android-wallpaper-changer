package org.gemini.wallpaper_changer;

import android.app.WallpaperManager;
import android.net.Uri;

import java.io.IOException;

public final class ClearWallpaperActivity extends WallpaperActivity
{
  @Override
  protected void processUri(WallpaperManager manager, Uri uri)
  {
    try { manager.clear(); }
    catch (IOException ex) {}
  }
}
