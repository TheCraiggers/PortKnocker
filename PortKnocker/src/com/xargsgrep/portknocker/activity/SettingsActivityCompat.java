/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 *
 * Ahsan Rabbani <ahsan@xargsgrep.com>
 *
 */
package com.xargsgrep.portknocker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.xargsgrep.portknocker.R;
import com.xargsgrep.portknocker.widget.HostWidget;

public class SettingsActivityCompat extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	
    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	getSupportActionBar().setSubtitle(getResources().getString(R.string.settings_subtitle));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case android.R.id.home: 
				Intent hostListIntent = new Intent(this, HostListActivity.class);
				hostListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(hostListIntent);
		        return true;
		    default:
		    	return super.onOptionsItemSelected(item);
    	}
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (getString(R.string.pref_key_hide_ports_widget).equals(key)) {
			HostWidget.updateAllAppWidgets(this);
		}
	}

}
