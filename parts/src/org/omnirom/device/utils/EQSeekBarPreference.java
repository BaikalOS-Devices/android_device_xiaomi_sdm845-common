/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.omnirom.device.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;

import android.os.Handler;
import android.os.Message;

import android.util.Log;


import org.omnirom.device.R;

public class EQSeekBarPreference extends Preference {

    public int minimum = -12;
    public int maximum = 12;
    public int def = 0;
    public int interval = 1;

    final int UPDATE = 0;

    int currentValue = def;

    private OnPreferenceChangeListener changer;

    public EQSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EQSeekBarPreference, 0, 0);

        minimum = typedArray.getInt(R.styleable.EQSeekBarPreference_min_value, minimum);
        maximum = typedArray.getInt(R.styleable.EQSeekBarPreference_max_value, maximum);
        def = typedArray.getInt(R.styleable.EQSeekBarPreference_default_value, def);

        typedArray.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    private void bind(final PreferenceViewHolder layout) {
        final SeekBar bar = (SeekBar) layout.findViewById(R.id.eq_seek_bar);

        bar.setMax(maximum - minimum);
        bar.setMin(0);
        bar.setProgress(currentValue - minimum);        

        Log.e("DiracEQ"," maximum=" + (maximum - minimum));
        Log.e("DiracEQ"," progress=" + (currentValue - minimum));

        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = Math.round(((float) progress) / interval) * interval;
                currentValue = progress + minimum;
                Log.e("DiracEQ"," progress=" + progress + ", currentValue" + (progress + minimum));
                changer.onPreferenceChange(EQSeekBarPreference.this, currentValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                
            }
        });
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder  view) {
        super.onBindViewHolder(view);
        bind(view);
    }

    public void setInitValue(int progress) {
        currentValue = progress;
    }

    @Override
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        changer = onPreferenceChangeListener;
        super.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }

    public int reset() {
        currentValue = 0;
        notifyChanged();
        return currentValue;
    }

    public void setValue(int progress) {
        currentValue = progress;
        notifyChanged();
    }
}
