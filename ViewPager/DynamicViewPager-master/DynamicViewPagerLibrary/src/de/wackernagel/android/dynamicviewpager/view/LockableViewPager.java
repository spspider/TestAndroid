package de.wackernagel.android.dynamicviewpager.view;

import de.wackernagel.android.dynamicviewpager.Lockable;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LockableViewPager extends ViewPager implements Lockable {
	private boolean locked;
	
	public LockableViewPager( final Context context ) {
		super(context);
		unlockSwiping();
	}
	
	public LockableViewPager( final Context context, final AttributeSet attrs ) {
		super(context, attrs);
		unlockSwiping();
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		if( !isLocked() ) {
			return super.onTouchEvent( event );
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent( MotionEvent event )
	{
		if( !isLocked() ) {
			return super.onInterceptTouchEvent( event );
		}
		return false;
	}

	@Override
	public void lockSwiping() {
		this.locked = true;
	}

	@Override
	public void unlockSwiping() {
		this.locked = false;
	}

	@Override
	public void toggleLock() {
		this.locked = !isLocked();
	}

	@Override
	public boolean isLocked() {
		return locked;
	}
}