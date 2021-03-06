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
package com.xargsgrep.portknocker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.xargsgrep.portknocker.R;
import com.xargsgrep.portknocker.activity.EditHostActivity;
import com.xargsgrep.portknocker.adapter.PortArrayAdapter;
import com.xargsgrep.portknocker.db.DatabaseManager;
import com.xargsgrep.portknocker.model.Host;
import com.xargsgrep.portknocker.model.Port;

public class PortsFragment extends SherlockListFragment {
	
	public static final String TAG = "PortsFragment";
	
    DatabaseManager databaseManager;
    PortArrayAdapter portAdapter;
    boolean savedInstanceState = false;
	
	public static PortsFragment newInstance(Long hostId) {
		PortsFragment fragment = new PortsFragment();
		if (hostId != null) {
			Bundle args = new Bundle();
			args.putLong(EditHostActivity.KEY_HOST_ID, hostId);
			fragment.setArguments(args);
		}
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		databaseManager = new DatabaseManager(getActivity());
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_view, container, false);
    	View header = inflater.inflate(R.layout.ports_header, null);
    	((LinearLayout) view).addView(header, 0);
    	return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
    	
    	Bundle args = getArguments();
    	
		List<Port> defaultPorts = new ArrayList<Port>();
		defaultPorts.add(new Port());
		defaultPorts.add(new Port());
		defaultPorts.add(new Port());
    	
    	if (args != null && !this.savedInstanceState) {
    		// only restore state from args if onSaveInstanceState hasn't been invoked
    		Long hostId = args.getLong(EditHostActivity.KEY_HOST_ID);
    		Host host = databaseManager.getHost(hostId);
    		List<Port> ports = (host.getPorts().size() > 0) ? host.getPorts() : defaultPorts;
			portAdapter = new PortArrayAdapter(getActivity(), ports);
			setListAdapter(portAdapter);
    	}
    	else if (portAdapter == null) {
			portAdapter = new PortArrayAdapter(getActivity(), defaultPorts);
			setListAdapter(portAdapter);
    	}
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
		menu.add(Menu.NONE, EditHostActivity.MENU_ITEM_ADD_PORT, 0, "Add Port").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case EditHostActivity.MENU_ITEM_ADD_PORT:
	    		addPort();
	    		return true;
	    	default:
	    		return false;
    	}
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
		savedInstanceState = true;
    }
    
    public void clearFoci() {
    	ListView view = getListView();
    	for (int i=0; i<view.getChildCount(); i++) {
    		View row = view.getChildAt(i);
    		row.findViewById(R.id.port_row_port).clearFocus();
    	}
    }
    
    private void addPort() {
    	PortArrayAdapter adapter = (PortArrayAdapter) getListAdapter();
    	adapter.add(new Port());
    }
    
}
