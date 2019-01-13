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
package org.omnirom.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_CATEGORY_DISPLAY = "display";
    private static final String KEY_CATEGORY_CAMERA = "camera_pref";
    private static final String QC_SYSTEM_PROPERTY = "persist.sys.le_fast_chrg_enable";
    private static final String SYSTEM_PROPERTY_CAMERA_FOCUS_FIX = "persist.camera.focus_fix";
    private static final String SYSTEM_PROPERTY_VOLTE_FIX = "persist.volte.fix";
    private static final String SYSTEM_PROPERTY_HALL_SRV = "persist.sys.hall_sensor";
    private static final String SYSTEM_PROPERTY_PS_FB_BOOST = "persist.ps.fb_boost";
    private static final String SYSTEM_PROPERTY_QFP_WUP = "persist.qfp.wup_display";
    private static final String SYSTEM_PROPERTY_QFP_ENABLE = "persist.qfp_enable";
    private static final String SYSTEM_PROPERTY_HW_0D_DISABLE = "persist.hw.0d_disable";

    final String KEY_DEVICE_DOZE = "device_doze";
    final String KEY_DEVICE_DOZE_PACKAGE_NAME = "org.lineageos.settings.doze";



    private Preference mKcalPref;
    private SwitchPreference mCameraFocusFix;
    private SwitchPreference mEnableQC;
    private SwitchPreference mVolteFix;
    private SwitchPreference mHallSrv;
    private SwitchPreference mFbBoost;
    private SwitchPreference mQfpWup;
    private SwitchPreference mQfpEnable;
    private SwitchPreference mHw0DDisable;

    private PreferenceCategory cameraCategory;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        mKcalPref = findPreference("kcal");
        if(  mKcalPref != null ) {
            mKcalPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getContext(), DisplayCalibration.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

        mEnableQC = (SwitchPreference) findPreference(QC_SYSTEM_PROPERTY);
        if( mEnableQC != null ) {
            mEnableQC.setChecked(SystemProperties.getBoolean(QC_SYSTEM_PROPERTY, false));
            mEnableQC.setOnPreferenceChangeListener(this);
        }

        PreferenceCategory cameraCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_CAMERA);
        if( cameraCategory != null ) { 
            if (isZl1()) {
                getPreferenceScreen().removePreference(cameraCategory);
            } else {
                mCameraFocusFix = (SwitchPreference) findPreference(SYSTEM_PROPERTY_CAMERA_FOCUS_FIX);
                if( mCameraFocusFix != null ) {
                    mCameraFocusFix.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_CAMERA_FOCUS_FIX, false));
                    mCameraFocusFix.setOnPreferenceChangeListener(this);
                }
            }
        }

        mVolteFix = (SwitchPreference) findPreference(SYSTEM_PROPERTY_VOLTE_FIX);
        if( mVolteFix != null ) {
            mVolteFix.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_VOLTE_FIX, false));
            mVolteFix.setOnPreferenceChangeListener(this);
        }

        mHallSrv = (SwitchPreference) findPreference(SYSTEM_PROPERTY_HALL_SRV);
        if( mHallSrv != null ) {
            mHallSrv.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_HALL_SRV, false));
            mHallSrv.setOnPreferenceChangeListener(this);
        }

        mQfpWup = (SwitchPreference) findPreference(SYSTEM_PROPERTY_PS_FB_BOOST);
        if( mQfpWup != null ) {
            mQfpWup.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_PS_FB_BOOST, false));
            mQfpWup.setOnPreferenceChangeListener(this);
        }

        mQfpWup = (SwitchPreference) findPreference(SYSTEM_PROPERTY_QFP_WUP);
        if( mQfpWup != null ) {
            if (isZl1()) {
                getPreferenceScreen().removePreference(mQfpWup);
            } else {
                mQfpWup.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_QFP_WUP, false));
                mQfpWup.setOnPreferenceChangeListener(this);
            }
        }

        mQfpEnable = (SwitchPreference) findPreference(SYSTEM_PROPERTY_QFP_ENABLE);
        if( mQfpEnable != null ) {
            if (isZl1()) {
                getPreferenceScreen().removePreference(mQfpWup);
            } else {
                mQfpEnable.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_QFP_ENABLE, false));
                mQfpEnable.setOnPreferenceChangeListener(this);
            }
        }




        mHw0DDisable = (SwitchPreference) findPreference(SYSTEM_PROPERTY_HW_0D_DISABLE);
        if( mHw0DDisable != null ) {
            mHw0DDisable.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_HW_0D_DISABLE, false));
            mHw0DDisable.setOnPreferenceChangeListener(this);
        }

        if (!isAppInstalled(KEY_DEVICE_DOZE_PACKAGE_NAME)) {
            PreferenceCategory displayCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_DISPLAY);
            displayCategory.removePreference(findPreference(KEY_DEVICE_DOZE));
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

    private boolean isZl1() {
        if(SystemProperties.get("ro.product.vendor.device").equals("zl1")) {
            return true;
        } else {
            return false;
        }
    }

    private void setEnable(String key, boolean value) {
        if(value) {
            SystemProperties.set(key, "1");
        } else {
            SystemProperties.set(key, "0");
        }
    }
}
