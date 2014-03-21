package com.ctx.he;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements MeterSelectorFragment.switchFragmentInterface {
	// Variables
	private String[] navDrawerItems;
	private DrawerLayout navDrawerLayout;
	private ListView navDrawerList;
	private ListView lvMeasures;
	ActionBarDrawerToggle navDrawerToggle;
	private CharSequence title;
	private CharSequence navDrawerTitle;
	private MeasureDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Insert Meter Selector Fragment
		Fragment fragment = new MeterSelectorFragment();
		Bundle args = new Bundle();
		args.putInt(MeterSelectorFragment.CODE_ID, 0);
		fragment.setArguments(args);
		setTitle("Monthly Running Costs");
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();

		// initialize the navigation list with a string array:
		navDrawerTitle = title = getResources().getString(R.string.nav_drawer);
		navDrawerItems = getResources().getStringArray(R.array.nav_drawer);
		navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navDrawerList = (ListView) findViewById(R.id.left_drawer);        
		navDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, navDrawerItems));
		navDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		navDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				navDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.open_drawer,  
				R.string.close_drawer 
				) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(title);
				//invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(navDrawerTitle);
				//invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		navDrawerLayout.setDrawerListener(navDrawerToggle);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (navDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {

		}
		return super.onOptionsItemSelected(item);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = navDrawerLayout.isDrawerOpen(navDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		navDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		navDrawerToggle.onConfigurationChanged(newConfig);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/* Change fragments according to selection */
	private void selectItem(int position) {
		FragmentManager fragmentManager;
		Fragment fragment = null;
		Bundle args = null;

		switch(position) {
		case 0:
			fragment = new MeterSelectorFragment();
			args = new Bundle();
			args.putInt(MeterSelectorFragment.CODE_ID, position);
			fragment.setArguments(args);
			setTitle("Available House Meters");
			// Insert the fragment by replacing any existing fragment
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();

			// Highlight the selected item, update the title, and close the drawer
			navDrawerList.setItemChecked(position, true);
			navDrawerLayout.closeDrawer(navDrawerList);
			break;
		case 1:
			fragment = new CostEditorFragment();
			setTitle("Current Meter Costs");
			// Insert the fragment by replacing any existing fragment
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();

			// Highlight the selected item, update the title, and close the drawer
			navDrawerList.setItemChecked(position, true);
			navDrawerLayout.closeDrawer(navDrawerList);
			break;		
		case 2:

			break;	
		case 3:
			// Erase Database
	        dao = new MeasureDAO(this);
	        dao.open();
	        dao.deleteAllMeasures();
	        navDrawerLayout.closeDrawer(navDrawerList);
			break;	
		case 4:
			

			break;	
		default:
			fragment = new MeterSelectorFragment();
			args = new Bundle();
			args.putInt(MeterSelectorFragment.CODE_ID, position);
			fragment.setArguments(args);
			break;						
		}


	}

	@Override
	public void setTitle(CharSequence _title) {
		title = _title;
		getActionBar().setTitle(title);
	}



	// Communication Interface from Fragments
	@Override
	public void switchFragment(int fragmentId) {
		String title = "Meter";
		int code = 0;

		switch (fragmentId) {
		case (R.id.layoutGas):
			title = "Gas";
			code = 1;
		break;
		case (R.id.layoutElectricity):
			title = "Electricity";
			code = 2;
		break;
		case (R.id.layoutWater):
			title = "Water";
			code = 3;
		break;
		case (R.id.layoutFixed):
			title = "Fixed";
			code = 4;
		break;
		}

		Fragment fragment = new MeterFragment();
		Bundle args = new Bundle();
		args.putInt(MeterFragment.CODE_ID, code);
		args.putString(MeterFragment.TITLE, title);
		fragment.setArguments(args);
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		setTitle(title);    

	}
}
