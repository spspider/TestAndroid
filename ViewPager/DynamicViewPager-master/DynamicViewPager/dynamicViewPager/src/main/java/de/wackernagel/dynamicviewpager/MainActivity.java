package de.wackernagel.dynamicviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.wackernagel.android.dynamicviewpager.adapter.DynamicTabFragmentPagerAdapter;


public class MainActivity extends ActionBarActivity {
	private DynamicTabFragmentPagerAdapter adapter;
	private ViewPager pager;
	private TextView log;
	private int pageCount = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// load views
		pager = (ViewPager) findViewById(R.id.viewPager);
		adapter = new DynamicTabFragmentPagerAdapter( this, getSupportFragmentManager(), pager, getSupportActionBar() );
		log = (TextView) findViewById(R.id.logView);
		
		// generate first tree fragments
		for( int position = 1; position <= pageCount; position++) {
			Bundle arguments = new Bundle();
			arguments.putString( SimpleFragment.CONTENT_KEY, String.valueOf( position ) );
			adapter.addPage( String.valueOf( position ), SimpleFragment.class, arguments );
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		// use one button for ViewPager locking
		MenuItem lockItem = menu.findItem(R.id.lock);
		//lockItem.setTitle( pager.isLocked() ? "Unlock" : "Lock" );
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case R.id.add:
				return addPage();
			case R.id.remove:
				return removePage();
			case R.id.replace:
				return replacePage();
			//case R.id.lock:
			//	return lockPager();
			//case R.id.log:
			//	return logPager();
			default:
				return super.onOptionsItemSelected( item );
		}
	}

	private boolean logPager() {
		String log = "";
		for( int position = 0; position < adapter.getCount(); position++ ) {
			Fragment f = adapter.getFragmentInstance( position );
			if( f instanceof SimpleFragment ) {
				log += ( (SimpleFragment) f).getContent();
			}
		}
		this.log.setText( "Log: " + log );
		return true;
	}

	private boolean lockPager() {
		//pager.toggleLock();
		supportInvalidateOptionsMenu();
		return true;
	}

	private boolean replacePage() {
		pageCount++;
		Bundle arguments = new Bundle();
		arguments.putString( SimpleFragment.CONTENT_KEY, String.valueOf( pageCount ) );
		//adapter.replacePage( getSupportActionBar().getSelectedNavigationIndex(), String.valueOf( pageCount ), SimpleFragment.class, arguments );
		return true;
	}

	private boolean removePage() {
		adapter.removePage( getSupportActionBar().getSelectedNavigationIndex() );
		return true;
	}

	private boolean addPage() {
		pageCount++;
		Bundle arguments = new Bundle();
		arguments.putString( SimpleFragment.CONTENT_KEY, String.valueOf( pageCount ) );
		adapter.addPage( String.valueOf( pageCount ), SimpleFragment.class, arguments );
		return true;
	}

}
