package android.support.v7.internal.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources.Theme;
import android.support.annotation.StyleRes;
import android.support.v7.appcompat.C0120R;
import android.view.LayoutInflater;

public class ContextThemeWrapper extends ContextWrapper {
    private LayoutInflater mInflater;
    private Theme mTheme;
    private int mThemeResource;

    public ContextThemeWrapper(Context base, @StyleRes int themeResId) {
        super(base);
        this.mThemeResource = themeResId;
    }

    public ContextThemeWrapper(Context base, Theme theme) {
        super(base);
        this.mTheme = theme;
    }

    public void setTheme(int resid) {
        if (this.mThemeResource != resid) {
            this.mThemeResource = resid;
            initializeTheme();
        }
    }

    public int getThemeResId() {
        return this.mThemeResource;
    }

    public Theme getTheme() {
        if (this.mTheme != null) {
            return this.mTheme;
        }
        if (this.mThemeResource == 0) {
            this.mThemeResource = C0120R.style.Theme_AppCompat_Light;
        }
        initializeTheme();
        return this.mTheme;
    }

    public Object getSystemService(String name) {
        if (!"layout_inflater".equals(name)) {
            return getBaseContext().getSystemService(name);
        }
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
        }
        return this.mInflater;
    }

    protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
        theme.applyStyle(resid, true);
    }

    private void initializeTheme() {
        boolean first = this.mTheme == null;
        if (first) {
            this.mTheme = getResources().newTheme();
            Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                this.mTheme.setTo(theme);
            }
        }
        onApplyThemeResource(this.mTheme, this.mThemeResource, first);
    }
}
