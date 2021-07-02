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

/* MODIFICATIONS
 * Changed the package name and some of the imports.
 * Changed most of the constants.
 * Extended the logic for checking and generating the bundle.
 * Adapted the javadoc comments.
 * Added some functions to read from the bundle.
 */

package de.bfrf.taskercmprofile.bundle;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import de.bfrf.taskercmprofile.Constants;

/**
 * Class for managing the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} for this plug-in.
 */
public final class PluginBundleManager
{
    /**
     * Type: {@code String}.
     * <p>
     * Name of the profile to activate.
     */
    public static final String BUNDLE_EXTRA_STRING_NAME = "de.bfrf.de.bfrf.taskercmprofile.extra.STRING_NAME"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p>
     * UUID of the profile to activate (as a String).
     */
    public static final String BUNDLE_EXTRA_STRING_UUID = "de.bfrf.de.bfrf.taskercmprofile.extra.STRING_UUID";

    /**
     * Type: {@code int}.
     * <p>
     * versionCode of the plug-in that saved the Bundle.
     */
    /*
     * This extra is not strictly required, however it makes backward and forward compatibility significantly
     * easier. For example, suppose a bug is found in how some version of the plug-in stored its Bundle. By
     * having the version, the plug-in can better detect when such bugs occur.
     */
    public static final String BUNDLE_EXTRA_INT_VERSION_CODE =
            "de.bfrf.de.bfrf.taskercmprofile.extra.INT_VERSION_CODE"; //$NON-NLS-1$

    /**
     * Method to verify the content of the bundle are correct.
     * <p>
     * This method will not mutate {@code bundle}.
     *
     * @param bundle bundle to verify. May be null, which will always return false.
     * @return true if the Bundle is valid, false if the bundle is invalid.
     */
    public static boolean isBundleValid(final Bundle bundle)
    {
        if (null == bundle)
        {
            return false;
        }

        /*
         * Make sure the expected extras exist
         */
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_NAME))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s", BUNDLE_EXTRA_STRING_NAME)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_UUID))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s", BUNDLE_EXTRA_STRING_UUID)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_INT_VERSION_CODE))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the correct number of extras exist. Run this test after checking for specific Bundle
         * extras above so that the error message is more useful. (E.g. the caller will see what extras are
         * missing, rather than just a message that there is the wrong number).
         */
        if (3 != bundle.keySet().size())
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain 3 keys, but currently contains %d keys: %s", bundle.keySet().size(), bundle.keySet())); //$NON-NLS-1$
            }
            return false;
        }

        if (TextUtils.isEmpty(bundle.getString(BUNDLE_EXTRA_STRING_NAME)))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle extra %s appears to be null or empty.  It must be a non-empty string", BUNDLE_EXTRA_STRING_NAME)); //$NON-NLS-1$
            }
            return false;
        }

        if (TextUtils.isEmpty(bundle.getString(BUNDLE_EXTRA_STRING_UUID)))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle extra %s appears to be null or empty.  It must be a non-empty string", BUNDLE_EXTRA_STRING_UUID)); //$NON-NLS-1$
            }
            return false;
        }

        if (bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 0) != bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 1))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle extra %s appears to be the wrong type.  It must be an int", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }

            return false;
        }

        return true;
    }

    /**
     * @param context Application context.
     * @param name The name of the profile to activate. Cannot be null.
     * @param uuid The UUID of the profile to activate (as a String). Cannot be null.
     * @return A plug-in bundle.
     */
    public static Bundle generateBundle(final Context context, final String name, final String uuid)
    {
        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(context));
        result.putString(BUNDLE_EXTRA_STRING_NAME, name);
        result.putString(BUNDLE_EXTRA_STRING_UUID, uuid);

        return result;
    }

    /**
     * @param bundle A valid bundle. Cannot be null.
     * @return The UUID stored in the bundle.
     */
    public static String getBundleExtraStringUuid(Bundle bundle) {
        return bundle.getString(BUNDLE_EXTRA_STRING_UUID, null);
    }

    /**
     * @param bundle A valid bundle. Cannot be null.
     * @return The profile name stored in the bundle.
     */
    public static String getBundleExtraStringName(Bundle bundle) {
        return bundle.getString(BUNDLE_EXTRA_STRING_NAME, null);
    }

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PluginBundleManager()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
