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

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.xargsgrep.portknocker.R;
import com.xargsgrep.portknocker.activity.HostListActivity;
import com.xargsgrep.portknocker.adapter.HostArrayAdapter;
import com.xargsgrep.portknocker.db.DatabaseManager;
import com.xargsgrep.portknocker.model.Host;
import com.xargsgrep.portknocker.widget.HostWidget;

public class HostListFragment extends SherlockListFragment {
	
	public static final String TAG = "HostListFragment";
	
    DatabaseManager databaseManager;
    int deleteHostPosition;
    
	public static HostListFragment newInstance() {
		return new HostListFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		databaseManager = new DatabaseManager(getActivity());
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_view, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
    	
        List<Host> hosts = databaseManager.getAllHosts();
		HostArrayAdapter hostAdapter = new HostArrayAdapter(this, hosts);
		setListAdapter(hostAdapter);
    }
    
	public void showDeleteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.confirm_dialog_delete_host_title);
        dialogBuilder.setIcon(R.drawable.ic_dialog_confirm);
        
        final int position = deleteHostPosition;
        dialogBuilder.setPositiveButton(R.string.confirm_dialog_confirm,
            new DialogInterface.OnClickListener() {
        		@SuppressWarnings("unchecked")
                public void onClick(DialogInterface dialog, int which) {
					Host host = (Host) getListAdapter().getItem(position);
					databaseManager.deleteHost(host);
                	((ArrayAdapter<Host>) getListAdapter()).remove(host);
			    	HostWidget.updateAllAppWidgetsForHost(getActivity(), host.getId());
                }
            }
        );
        dialogBuilder.setNegativeButton(R.string.confirm_dialog_cancel,
    		new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) { }
    		}
        );
        
        AlertDialog deleteDialog = dialogBuilder.create();
        ((HostListActivity) getActivity()).setDeleteDialog(deleteDialog);
        deleteDialog.show();
	}
	
    public void setDeleteHostPosition(int position) {
    	this.deleteHostPosition = position;
    }
    
}
