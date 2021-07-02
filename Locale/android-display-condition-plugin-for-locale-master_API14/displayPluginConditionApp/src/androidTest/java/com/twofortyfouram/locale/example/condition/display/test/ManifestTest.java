/*
 * android-display-condition-plugin-for-locale <https://github.com/twofortyfouram/android-display-condition-plugin-for-locale>
 * Copyright 2014 two forty four a.m. LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twofortyfouram.locale.example.condition.display.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests to verify proper entries in the plug-in's Android Manifest.
 */
public final class ManifestTest extends AndroidTestCase {

    @SmallTest
    public void testApplicationEnabled() {
        assertTrue(getContext().getApplicationInfo().enabled);
    }

    @SmallTest
    public void testPluginActivityPresent() {
        final PackageManager packageManager = getContext().getPackageManager();

        final List<ResolveInfo> activities = getPluginActivities(getContext());
        assertFalse(activities.isEmpty());

        for (final ResolveInfo x : activities) {
            assertTrue(x.activityInfo.enabled);
            assertTrue(x.activityInfo.exported);

            /*
             * Verify that the plug-in doesn't request permissions not available to the host
             */
            assertNull(x.activityInfo.permission);

            /*
             * Verify that the plug-in has a label attribute in the AndroidManifest
             */
            assertFalse(0 == x.activityInfo.labelRes);

            /*
             * Verify that the plug-in has a icon attribute in the AndroidManifest
             */
            assertFalse(0 == x.activityInfo.icon);
        }
    }

    @SmallTest
    public void testPluginReceiver() {
        final PackageManager packageManager = getContext().getPackageManager();

        final List<ResolveInfo> receivers = getPluginReceivers(getContext());

        assertEquals(1, receivers.size());

        for (final ResolveInfo x : receivers) {
            assertTrue(x.activityInfo.enabled);
            assertTrue(x.activityInfo.exported);

            /*
             * Verify that the plug-in doesn't request permissions not available to the host
             */
            assertNull(x.activityInfo.permission);
        }
    }

    /**
     * Verifies the package is configured to be installed to internal memory
     */
    @SmallTest
    public void testManifestInstallLocation() throws Exception {
        /*
         * Note that in addition to this test, Locale will also check that a plug-in is actually on
         * internal memory at runtime. This primarily affects custom ROMs that permit moving apps to
         * external memory even if the app specifies internalOnly.
         */
        assertEquals(InstallLocation.internalOnly, InstallLocation.getManifestInstallLocation(
                getContext(), getContext().getPackageName()));
    }

    /**
     * Gets a list of all Activities in {@code context}'s package that export
     * {@link com.twofortyfouram.locale.api.Intent#ACTION_EDIT_CONDITION ACTION_EDIT_CONDITION}.
     *
     * @param context Application context.
     */
    private static List<ResolveInfo> getPluginActivities(@NonNull final Context context) {

        final String packageName = context.getPackageName();

        final List<ResolveInfo> result = new LinkedList<ResolveInfo>();

        for (final ResolveInfo x : context.getPackageManager().queryIntentActivities(
                new Intent(com.twofortyfouram.locale.api.Intent.ACTION_EDIT_CONDITION),
                0)) {
            if (packageName.equals(x.activityInfo.packageName)) {
                result.add(x);
            }
        }

        return result;
    }

    /**
     * Gets a list of all BroadcastReceivers in {@code context}'s package that export
     * {@link com.twofortyfouram.locale.api.Intent#ACTION_QUERY_CONDITION ACTION_QUERY_CONDITION}.
     *
     * @param context Application context.
     */
    private static List<ResolveInfo> getPluginReceivers(@NonNull final Context context) {
        final String packageName = context.getPackageName();

        final List<ResolveInfo> result = new LinkedList<ResolveInfo>();

        for (final ResolveInfo x : context.getPackageManager().queryBroadcastReceivers(
                new Intent(com.twofortyfouram.locale.api.Intent.ACTION_QUERY_CONDITION),
                0)) {
            if (packageName.equals(x.activityInfo.packageName)) {
                result.add(x);
            }

        }

        return result;
    }
}
