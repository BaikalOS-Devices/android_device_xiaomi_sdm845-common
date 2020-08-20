package org.lineageos.settings.device;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import org.lineageos.settings.device.DiracAudioEnhancerUtils;
import android.util.Log;

public class DiracAudioEnhancerService extends Service {

   public static DiracAudioEnhancerUtils du;

   @Override
   public IBinder onBind(Intent arg0) {
      return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      Log.i("DiracAudioEnhancerService","onStartCommand");
      // Let it continue running until it is stopped.
      // if( du != null ) return START_STICKY;
      try {
        du = new DiracAudioEnhancerUtils();
        Log.i("DiracAudioEnhancerService","initialize");
        du.initialize();
      } catch(Exception e) {
        Log.e("DiracAudioEnhancerService","Exception:",e);
        du = null;
      }
      //Toast.makeText(this, "Audio Enhancer Started", Toast.LENGTH_LONG).show();
      Log.i("DiracAudioEnhancerService","START_STICKY");
      return START_STICKY;
   }


   @Override
   public void onDestroy() {
      super.onDestroy();
      Log.i("DiracAudioEnhancerService","onDestroy");
      //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
   }
}
