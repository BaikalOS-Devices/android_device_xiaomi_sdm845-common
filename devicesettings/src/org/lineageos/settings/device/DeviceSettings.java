/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.lineageos.settings.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

import androidx.preference.PreferenceFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import java.util.Arrays;

import org.lineageos.settings.device.utils.EQSeekBarPreference;
import org.lineageos.settings.device.utils.FileUtils;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_CATEGORY_DISPLAY = "display";
    private static final String KEY_CATEGORY_CAMERA = "camera_pref";

    private static final String SYSTEM_PROPERTY_SND_COMP = "persist.baikal.compander";
    private static final String SYSTEM_PROPERTY_SND_MIC = "persist.baikal.mic_gain";
    private static final String SYSTEM_PROPERTY_SND_EARPIECE = "persist.baikal.earpiece_gain";
    private static final String SYSTEM_PROPERTY_SND_HP = "persist.baikal.headphone_gain";

    private static final String SYSTEM_PROPERTY_NVT_FW = "persist.baikalos.nvt_fw";

    private static final String KEY_SND_COMP = "snd_comp";    
    private static final String KEY_SND_MIC = "snd_mic";    
    private static final String KEY_SND_EARPIECE = "snd_earpiece";    
    private static final String KEY_SND_HP = "snd_hp";    

    private static final String KEY_DIRAC_SOUND_ENHANCER = "dirac_enhancer";
    private static final String KEY_DIRAC_HEADSET_TYPE = "dirac_headsets";
    private static final String KEY_DIRAC_MOVIE_MODE = "dirac_mode";
    private static final String KEY_DIRAC_PRESET = "dirac_preset";



    private static final String KEY_DIRAC_EQ_BAND="dirac_eq_band_";

    final String KEY_DEVICE_DOZE = "device_doze";
    final String KEY_DEVICE_DOZE_PACKAGE_NAME = "org.lineageos.settings.doze";


    private TwoStatePreference mDiracEnableDisable;
    private TwoStatePreference mDiracMovieMode;
    private TwoStatePreference mDiracCommand;
    private ListPreference mDiracHeadsetType, mDiracPreset;


    private Preference mKcalPref;

    private SwitchPreference mNvtFw;

    private SwitchPreference mSndComp;
    private SwitchPreference mSndMic;
    private SwitchPreference mSndEarPiece;
    private SwitchPreference mSndHP;

    private PreferenceCategory mEqCategory;


    private EQSeekBarPreference mEqBand0;
    private EQSeekBarPreference mEqBand1;
    private EQSeekBarPreference mEqBand2;
    private EQSeekBarPreference mEqBand3;
    private EQSeekBarPreference mEqBand4;
    private EQSeekBarPreference mEqBand5;
    private EQSeekBarPreference mEqBand6;

    private int[] mCustomEq = { 0, 0, 0, 0, 0, 0, 0 };

    private String[] mEqPresets = { 
        "0,0,0,0,0,0,0",
        "4,2,-2,0,-2,-2,4",
        "0,0,0,-2,-3,0,0",
        "0,-3,-5,0,0,-3,0",
        "0,0,0,0,3,6,6",
        "3,3,-3,0,-3,0,2",
        "2,4,-6,4,0,1,2",
        "3,3,-1,0,-3,0,0",
        "0,0,-2,-2,2,2,0",
        "0,4,2,0,-2,-2,4",
        "2,0,0,-2,-4,0,0" };


    boolean mIsCustomEq = false;

    private Context mContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        mContext = getActivity();

        mKcalPref = findPreference("xiaomi_display");
        if(  mKcalPref != null ) {
            mKcalPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getContext(), XiaomiDisplay.class);
                    startActivity(intent);
                    return true;
                }
            });
        }


        mDiracEnableDisable = (TwoStatePreference) findPreference(KEY_DIRAC_SOUND_ENHANCER);


        if (DiracAudioEnhancerService.du == null ) {
            mContext.startService(new Intent(mContext, DiracAudioEnhancerService.class));
        }

        if (DiracAudioEnhancerService.du != null && DiracAudioEnhancerService.du.hasInitialized()) {
          mDiracEnableDisable.setChecked(DiracAudioEnhancerService.du.isEnabled(mContext) ? true:false);
        }
        mDiracEnableDisable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {
            //if(((TwoStatePreference) preference).isChecked() != (Boolean) newValue) {
              DiracAudioEnhancerService.du.setEnabled(mContext, (Boolean) newValue ? true:false);
            //}
            return true;
          }
        });

        mDiracHeadsetType = (ListPreference) findPreference(KEY_DIRAC_HEADSET_TYPE);
        mDiracPreset = (ListPreference) findPreference(KEY_DIRAC_PRESET);


        if (DiracAudioEnhancerService.du != null && DiracAudioEnhancerService.du.hasInitialized()) {
            mDiracHeadsetType.setValue(Integer.toString(DiracAudioEnhancerService.du.getHeadsetType(mContext)));
        }
        mDiracHeadsetType.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {
            int val = Integer.parseInt(newValue.toString());
            DiracAudioEnhancerService.du.setHeadsetType(mContext, val);
            return true;
          }
        });


        mDiracCommand = (TwoStatePreference) findPreference("dirac_command");
        if( mDiracCommand != null ) {
            mDiracCommand.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
              public boolean onPreferenceChange(Preference preference, Object newValue) {
                int param,value;
                param = Integer.parseInt(SystemProperties.get("persist.dirac.cmd.param","-1"));
                value = Integer.parseInt(SystemProperties.get("persist.dirac.cmd.val","-1"));
                DiracAudioEnhancerService.du.setParam(mContext, param, value);
                return true;
              }
            });
        }


        mDiracMovieMode = (TwoStatePreference) findPreference(KEY_DIRAC_MOVIE_MODE);
        if( mDiracMovieMode != null ) {

            if (DiracAudioEnhancerService.du != null && DiracAudioEnhancerService.du.hasInitialized()) {
                mDiracMovieMode.setChecked(DiracAudioEnhancerService.du.getMode(mContext) !=0 ? true:false);
            }
       
            mDiracMovieMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
              public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean val = (Boolean)newValue;
                DiracAudioEnhancerService.du.setMode(mContext, val? 1:0);
                return true;
              }
            });

        }


        mEqCategory = (PreferenceCategory) findPreference("dirac_eq_settings");

        mEqBand0 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "0");
        mEqBand1 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "1");
        mEqBand2 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "2");
        mEqBand3 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "3");
        mEqBand4 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "4");
        mEqBand5 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "5");
        mEqBand6 = (EQSeekBarPreference) findPreference(KEY_DIRAC_EQ_BAND + "6");

        mEqBand0.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(0," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(0, (int)newValue);
            updateCustomEq();
            return true;
          }
        });

        mEqBand1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(1," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(1, (int)newValue);
            updateCustomEq();
            return true;
          }
        });

        mEqBand2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(2," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(2, (int)newValue);
            updateCustomEq();
            return true;
          }
        });

        mEqBand3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(3," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(3, (int)newValue);
            updateCustomEq();
            return true;
          }
        });

        mEqBand4.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(4," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(4, (int)newValue);
            updateCustomEq();
            return true;
          }
        });

        mEqBand5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(5," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(5, (int)newValue);
            updateCustomEq();
            return true;
          }
        });

        mEqBand6.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {

            Log.e("DiracEQ"," setValue(6," + newValue + ")");

            DiracAudioEnhancerService.du.setLevel(6, (int)newValue);
            updateCustomEq();
            return true;
          }
        });


        if (DiracAudioEnhancerService.du != null && DiracAudioEnhancerService.du.hasInitialized()) {

            if( SystemProperties.get("persist.dirac.custom","0").equals("1") ){
                mDiracPreset.setValue("-1");
                mIsCustomEq = true;
                mEqCategory.setEnabled(true);
                DiracAudioEnhancerService.du.setLevel(SystemProperties.get("persist.dirac.eq","0,0,0,0,0,0,0"));
            } else {
                try {
                    int index = Arrays.asList(mEqPresets).indexOf(DiracAudioEnhancerService.du.getLevel());
                    if( index == -1  ) {
                        index = 0;
                    } 
                    mDiracPreset.setValue(String.valueOf(index));
                    //SystemProperties.set("persist.dirac.eq",mEqPresets[index]);
                    mIsCustomEq = false;
                    mEqCategory.setEnabled(false);
                    SystemProperties.set("persist.dirac.custom","0");
                } catch (Exception e) {
                    int index = 0;
                    mDiracPreset.setValue(String.valueOf(index));
                    //SystemProperties.set("persist.dirac.eq",mEqPresets[index]);
                    mIsCustomEq = false;
                    mEqCategory.setEnabled(false);
                    SystemProperties.set("persist.dirac.custom","0");
                }
            }
    

            mEqBand0.setValue(DiracAudioEnhancerService.du.getLevel(0));
            mEqBand1.setValue(DiracAudioEnhancerService.du.getLevel(1));
            mEqBand2.setValue(DiracAudioEnhancerService.du.getLevel(2));
            mEqBand3.setValue(DiracAudioEnhancerService.du.getLevel(3));
            mEqBand4.setValue(DiracAudioEnhancerService.du.getLevel(4));
            mEqBand5.setValue(DiracAudioEnhancerService.du.getLevel(5));
            mEqBand6.setValue(DiracAudioEnhancerService.du.getLevel(6));

        }

        mDiracPreset.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          public boolean onPreferenceChange(Preference preference, Object newValue) {


            if( String.valueOf(newValue).equals("-1") ) {
                Log.e("DiracEQ"," preset=" + newValue);
                mIsCustomEq = true;
                mEqCategory.setEnabled(true);
                SystemProperties.set("persist.dirac.custom","1");
                DiracAudioEnhancerService.du.setLevel(SystemProperties.get("persist.dirac.eq","0,0,0,0,0,0,0"));
            } 
            else {
                Log.e("DiracEQ"," preset=" + newValue);
                String sIndex = String.valueOf(newValue);
                int index = Integer.parseInt(sIndex);
                mIsCustomEq = false;
                mEqCategory.setEnabled(false);
                SystemProperties.set("persist.dirac.custom","0");
                //SystemProperties.set("persist.dirac.eq",mEqPresets[index]);
                DiracAudioEnhancerService.du.setLevel(mEqPresets[index]);
                
            }


            mEqBand0.setValue(DiracAudioEnhancerService.du.getLevel(0));
            mEqBand1.setValue(DiracAudioEnhancerService.du.getLevel(1));
            mEqBand2.setValue(DiracAudioEnhancerService.du.getLevel(2));
            mEqBand3.setValue(DiracAudioEnhancerService.du.getLevel(3));
            mEqBand4.setValue(DiracAudioEnhancerService.du.getLevel(4));
            mEqBand5.setValue(DiracAudioEnhancerService.du.getLevel(5));
            mEqBand6.setValue(DiracAudioEnhancerService.du.getLevel(6));

            return true;
          }
        });


        mNvtFw = (SwitchPreference) findPreference(SYSTEM_PROPERTY_NVT_FW);
        if( mNvtFw != null ) {

            if( !FileUtils.fileExists("/sys/devices/platform/soc/a98000.i2c/i2c-3/3-0062/fw_variant") ) {
                mNvtFw.setVisible(false);
            } else {
                mNvtFw.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_NVT_FW, false));
                mNvtFw.setOnPreferenceChangeListener(this);
            }
        }


        mSndComp = (SwitchPreference) findPreference(SYSTEM_PROPERTY_SND_COMP);
        if( mSndComp != null ) {
            mSndComp.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_SND_COMP, false));
            mSndComp.setOnPreferenceChangeListener(this);
        }

        mSndMic = (SwitchPreference) findPreference(SYSTEM_PROPERTY_SND_MIC);
        if( mSndMic != null ) {
            mSndMic.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_SND_MIC, false));
            mSndMic.setOnPreferenceChangeListener(this);
        }

        mSndEarPiece = (SwitchPreference) findPreference(SYSTEM_PROPERTY_SND_EARPIECE);
        if( mSndEarPiece != null ) {
            mSndEarPiece.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_SND_EARPIECE, false));
            mSndEarPiece.setOnPreferenceChangeListener(this);
        }


        mSndHP = (SwitchPreference) findPreference(SYSTEM_PROPERTY_SND_HP);
        if( mSndHP != null ) {
            mSndHP.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_SND_HP, false));
            mSndHP.setOnPreferenceChangeListener(this);
        }

        if (!isAppInstalled(KEY_DEVICE_DOZE_PACKAGE_NAME)) {
            PreferenceCategory displayCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_DISPLAY);
            if( displayCategory != null ) {
                Preference dozePreference = (Preference)findPreference(KEY_DEVICE_DOZE);
                if( dozePreference != null ) {
                    displayCategory.removePreference(dozePreference);
                }
            }
        }

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        boolean value;
        value = (Boolean) newValue;
        ((SwitchPreference)preference).setChecked(value);
        setEnable(key,value);
        return true;
    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void setEnable(String key, boolean value) {
        if(value) {
            SystemProperties.set(key, "1");
        } else {
            SystemProperties.set(key, "0");
        }
    }

    private void updateCustomEq()
    {
        if( !mIsCustomEq ) return;
        String newValues = ""
        + DiracAudioEnhancerService.du.getLevel(0) + ","
        + DiracAudioEnhancerService.du.getLevel(1) + ","
        + DiracAudioEnhancerService.du.getLevel(2) + ","
        + DiracAudioEnhancerService.du.getLevel(3) + ","
        + DiracAudioEnhancerService.du.getLevel(4) + ","
        + DiracAudioEnhancerService.du.getLevel(5) + ","
        + DiracAudioEnhancerService.du.getLevel(6);

        SystemProperties.set("persist.dirac.eq",newValues);
    }
}
