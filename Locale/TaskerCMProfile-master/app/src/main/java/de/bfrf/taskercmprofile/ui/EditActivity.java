/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 * Copyright (C) 2015 Benedikt Rascher-Friesenhausen <benediktrascherfriesenhausen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

/*
 * MODIFICATIONS
 * Changed the package name and some of the imports.
 * Changed most of the logic in onCreate() and finish().
 * Added the class ProfileAdapter.
 */

package de.bfrf.taskercmprofile.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;

import java.util.UUID;

import cyanogenmod.app.Profile;
import cyanogenmod.app.ProfileManager;

import cyanogenmod.os.Build;
import de.bfrf.taskercmprofile.R;
import de.bfrf.taskercmprofile.bundle.BundleScrubber;
import de.bfrf.taskercmprofile.bundle.PluginBundleManager;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 * <p>
 * This Activity can be started in one of two states:
 * <ul>
 * <li>New plug-in instance: The Activity's Intent will not contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE}.</li>
 * <li>Old plug-in instance: The Activity's Intent will contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} from a previously saved plug-in instance that the
 * user is editing.</li>
 * </ul>
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_EDIT_SETTING
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class EditActivity extends AbstractPluginActivity
{

    private Spinner spinner;
    private ProfileAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);

        setContentView(R.layout.main);

        spinner = (Spinner) findViewById(R.id.ProfileSpinner);
        adapter = new ProfileAdapter(this);
        spinner.setAdapter(adapter);

        if (null == savedInstanceState)
        {
            if (PluginBundleManager.isBundleValid(localeBundle))
            {
                String uuid = PluginBundleManager.getBundleExtraStringUuid(localeBundle);
                int pos = adapter.getPositionByUuid(uuid);
                if (pos != -1) {
                    spinner.setSelection(pos);
                }
            }
        }
    }

    @Override
    public void finish() {
        if (!isCanceled()) {
            final int pos = spinner.getSelectedItemPosition();

            if (pos != -1) {
                final Profile profile = adapter.getItem(pos);
                final Intent resultIntent = new Intent();
                final Bundle resultBundle = PluginBundleManager.generateBundle(getApplicationContext(),
                        profile.getName(), profile.getUuid().toString());
                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, profile.getName());

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }
}

class ProfileAdapter extends BaseAdapter {

    private Profile[] profiles;
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return profiles.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView name;

        if (view == null) {
            view = inflater.inflate(R.layout.profile_spinner_item, null);
        }

        name = (TextView) view.findViewById(R.id.profile_item_name);

        if (getCount() <= 0) {
            name.setText(R.string.no_profiles);
        } else {
            name.setText(profiles[position].getName());
        }

        return view;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView name;

        if (view == null) {
            view = inflater.inflate(R.layout.profile_spinner_item, null);
        }

        name = (TextView) view.findViewById(R.id.profile_item_name);

        if (getCount() <= 0) {
            name.setText(R.string.no_profiles);
        } else {
            name.setText(profiles[position].getName());
        }

        return view;
    }

    @Override
    public Profile getItem(int position) {
        return profiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPositionByUuid(String uuid) {
        UUID id = UUID.fromString(uuid);
        for (int i = 0; i < profiles.length; ++i) {
            if (id.equals(profiles[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    public ProfileAdapter(Activity activity) {
        // Make sure Profiles API is available.
        if (Build.CM_VERSION.SDK_INT >= Build.CM_VERSION_CODES.BOYSENBERRY) {
            ProfileManager manager = ProfileManager.getInstance(activity);
            this.profiles = manager.getProfiles();
        } else {
            this.profiles = new Profile[0];
        }
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
