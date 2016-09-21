package org.gemini.wallpaper_changer;

import android.app.Activity;
import android.app.WallpaperManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

public abstract class WallpaperActivity extends Activity
{
  protected static final boolean LOGGING = false;
  private static final String TAG = WallpaperActivity.class.getSimpleName();

  protected static void debugLog(String TAG, String msg)
  {
    if (LOGGING) Log.i(TAG, msg);
  }

  @Override
  protected final void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    if (getIntent() == null)
    {
      Log.e(TAG, "No intent attached.");
      finish();
      return;
    }
    Uri uri = getIntent().getData();
    if (uri == null)
    {
      Log.e(TAG, "No uri attached.");
      finish();
      return;
    }
    WallpaperManager manager = WallpaperManager.getInstance(this);
    if (manager == null)
    {
      Log.e(TAG, "Cannot retrieve WallpaperManager.");
      finish();
      return;
    }
    processUri(manager, uri);
    finish();
  }

  @Override
  protected final void onDestroy()
  {
    super.onDestroy();
    Process.killProcess(Process.myPid());
    System.exit(0);
  }

  protected abstract void processUri(WallpaperManager manager, Uri uri);
}
