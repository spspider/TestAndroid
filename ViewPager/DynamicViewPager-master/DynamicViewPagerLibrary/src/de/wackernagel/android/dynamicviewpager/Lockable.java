package de.wackernagel.android.dynamicviewpager;

public interface Lockable {
	void lockSwiping();
	void unlockSwiping();
	void toggleLock();
	boolean isLocked();
}
