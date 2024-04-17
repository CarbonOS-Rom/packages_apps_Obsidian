/*
 * Copyright (C) 2026 CarbonOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.carbon.obsidian;

import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import com.carbon.obsidian.utils.DeviceUtils;
import com.carbon.obsidian.utils.PreferenceUtils;

import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class Facets extends SettingsPreferenceFragment {

    private static final String KEY_ICONS_CATEGORY = "themes_icons_category";
    private static final String KEY_SIGNAL_ICON = "android.theme.customization.signal_icon";

    private PreferenceCategory mIconsCategory;
    private Preference mSignalIcon;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.category_facets);

        mIconsCategory = findPreference(KEY_ICONS_CATEGORY);
        mSignalIcon = findPreference(KEY_SIGNAL_ICON);

        if (mIconsCategory != null && mSignalIcon != null
                && !DeviceUtils.deviceSupportsMobileData(getContext())) {
            mIconsCategory.removePreference(mSignalIcon);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceUtils.reloadCustomPrimarySwitches(getPreferenceScreen());
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.OBSIDIAN;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.category_facets;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    if (!DeviceUtils.deviceSupportsMobileData(context)) {
                        keys.add(KEY_SIGNAL_ICON);
                    }
                    return keys;
                }
            };
}