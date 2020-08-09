package org.lineageos.settings.device;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import java.util.List;
import java.util.*;
import org.lineageos.settings.device.DiracAudioEnhancer;

public class DiracAudioEnhancerUtils
{
  private DiracAudioEnhancer mAudEnhncr;
  private boolean mInitialized;


  public int getHeadsetType(Context paramContext)
  {
    return mAudEnhncr.getHeadsetType();
  }

  public boolean hasInitialized()
  {
    return mInitialized;
  }

  public void initialize()
  {
    boolean enabled;
    int iEnabled;
    if (!mInitialized)
    {
      mAudEnhncr = new DiracAudioEnhancer(0, 0);
      iEnabled = mAudEnhncr.getMusic();
      if (iEnabled == 1) {
        enabled = true;
      }else {
        enabled = false;
      }
      mAudEnhncr.setEnabled(enabled);
      mInitialized = true;
    }
  }

  public boolean isEnabled(Context paramContext)
  {
    int i =1;
    int j = 0;
    try {
        j = mAudEnhncr.getMusic();
        if (i == j)
        {
          return true;
        } else {
          return false;
        }
    } catch(Exception e) {
        return false;
    }
  }

  public void release()
  {
    if (mInitialized)
    {
      mAudEnhncr.release();
      mAudEnhncr = null;
      mInitialized = false;
    }
  }

  public void setEnabled(Context paramContext, boolean paramBoolean)
  {
    int i = 1;
    if (paramBoolean)
    {
      i = 1;
    } else {
      i = 0;
    }
    mAudEnhncr.setMusic(i);
    mAudEnhncr.setEnabled(paramBoolean);
    return;
  }

  public void setParam(Context paramContext, int paramInt, int value)
  {
    mAudEnhncr.setParam(paramInt,value);
  }

  public void setHeadsetType(Context paramContext, int paramInt)
  {
    mAudEnhncr.setHeadsetType(paramInt);
  }


  public void setMode(Context paramContext, int paramInt)
  {
    mAudEnhncr.setMode(paramInt);
  }

  public int getMode(Context paramContext)
  {
    return mAudEnhncr.getMode();
  }

    protected void setLevel(String preset) {
        String[] level = preset.split("\\s*,\\s*");
        for (int band = 0; band <= level.length - 1; band++) {
            mAudEnhncr.setLevel(band, Float.valueOf(level[band]));
        }
    }
	
	
    protected String getLevel() {
        String selected = "";
        for (int band = 0; band <= 6; band++) {
            int temp = (int) mAudEnhncr.getLevel(band);
            selected += String.valueOf(temp);
            if (band != 6) selected += ",";
        }
        return selected;
    }

    protected void setLevel(int band, int level) {
        if( band < 0 || band > 6 ) return;
        mAudEnhncr.setLevel(band,(float)level);
    }

    protected int getLevel(int band) {
        if( band < 0 || band > 6 ) return 0;
        return (int) mAudEnhncr.getLevel(band);
    }


}

