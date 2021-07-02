package com.mtetno.DynamicViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private ViewPager pager = null;
    private MainPagerAdapter pagerAdapter = null;

    // -----------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testfav);

        // ... do other initialization, such as create an ActionBar ...

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);

        WebView w1 = new WebView(MainActivity.this);

        w1.setWebViewClient(new WebViewClient());
        w1.loadUrl("http://www.google.com");

        WebView w2 = new WebView(MainActivity.this);

        w2.setWebViewClient(new WebViewClient());
        w2.loadUrl("http://www.live.com");

        pagerAdapter.addView(w1, 0);
        pagerAdapter.addView(w2, 1);

        pagerAdapter.notifyDataSetChanged();

        // Create an initial view to display; must be a subclass of FrameLayout.
        // FrameLayout v0 = (FrameLayout) inflater.inflate
        // (R.layout.one_of_my_page_layouts, null);
        // pagerAdapter.addView (v0, 0);
    }

    // -----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        int pageIndex = pagerAdapter.addView(newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem(pageIndex, true);
    }

    // -----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        // You might want to choose what page to display, if the current page
        // was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(pageIndex);
    }

    // -----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    // -----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.
    // "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

//    private void createMyView(String type, String url) {
//
//        url = "https://developers.google.com/android/nexus/images";
//
//    }

}