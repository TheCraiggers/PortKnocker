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
package com.xargsgrep.portknocker.adapter;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xargsgrep.portknocker.R;
import com.xargsgrep.portknocker.activity.EditHostActivity;
import com.xargsgrep.portknocker.asynctask.KnockerAsyncTask;
import com.xargsgrep.portknocker.db.DatabaseManager;
import com.xargsgrep.portknocker.fragment.HostListFragment;
import com.xargsgrep.portknocker.model.Host;

public class HostArrayAdapter extends ArrayAdapter<Host> {
	
    DatabaseManager databaseManager;
	Fragment fragment;
	List<Host> hosts;

	public HostArrayAdapter(Fragment fragment, List<Host> hosts) {
		super(fragment.getActivity(), -1, hosts);
        databaseManager = new DatabaseManager(fragment.getActivity());
		this.fragment = fragment;
		this.hosts = hosts;
	}
	
	@Override
	public int getCount() {
		return hosts.size();
	}
	
	@Override
	public Host getItem(int position) {
		return hosts.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) view = LayoutInflater.from(getContext()).inflate(R.layout.host_row, null);
		
		TextView labelView = (TextView) view.findViewById(R.id.host_row_label);
		TextView hostnameView = (TextView) view.findViewById(R.id.host_row_hostname);
		TextView portsView = (TextView) view.findViewById(R.id.host_row_ports);
		
		Host host = hosts.get(position);
		
		labelView.setText(host.getLabel());
		hostnameView.setText(host.getHostname());
		portsView.setText(host.getPortsString());
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
		if (sharedPreferences.getBoolean(fragment.getActivity().getString(R.string.pref_key_hide_ports), false)) {
			portsView.setVisibility(View.GONE);
		}
		
		final int fPosition = position;
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Host host = getItem(fPosition);
				KnockerAsyncTask knockerAsyncTask = new KnockerAsyncTask((FragmentActivity) fragment.getActivity(), host.getPorts().size());
				knockerAsyncTask.execute(host);
			}
		});
		
		ImageButton deleteButton = (ImageButton) view.findViewById(R.id.host_row_delete);
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((HostListFragment) fragment).setDeleteHostPosition(fPosition);
				((HostListFragment) fragment).showDeleteDialog();
			}
		});
		
		ImageButton editButton = (ImageButton) view.findViewById(R.id.host_row_edit);
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Host host = hosts.get(fPosition);
				Intent editHostIntent = new Intent(fragment.getActivity(), EditHostActivity.class);
				editHostIntent.putExtra(EditHostActivity.KEY_HOST_ID, host.getId());
		        fragment.getActivity().startActivity(editHostIntent);
			}
		});
		
		return view;
	}
	
}
