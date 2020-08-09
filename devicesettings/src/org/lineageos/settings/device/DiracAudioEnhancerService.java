package org.lineageos.settings.device;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import org.lineageos.settings.device.DiracAudioEnhancerUtils;

public class DiracAudioEnhancerService extends Service {

   public static DiracAudioEnhancerUtils du;

   @Override
   public IBinder onBind(Intent arg0) {
      return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      // Let it continue running until it is stopped.
      if( du != null ) return START_STICKY;
      try {
        du = new DiracAudioEnhancerUtils();
        du.initialize();
      } catch(Exception e) {
        du = null;
      }
      //Toast.makeText(this, "Audio Enhancer Started", Toast.LENGTH_LONG).show();
      return START_STICKY;
   }


   @Override
   public void onDestroy() {
      super.onDestroy();
      Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
   }
}
