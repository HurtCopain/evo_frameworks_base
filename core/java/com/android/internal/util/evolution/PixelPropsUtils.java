/*
 * Copyright (C) 2020 The Pixel Experience Project
 *               2022 StatiXOS
 *               2021-2022 crDroid Android Project
 *               2019-2023 Evolution X
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.evolution;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PixelPropsUtils {

    private static final String PACKAGE_FINSKY = "com.android.vending";
    private static final String PACKAGE_GMS = "com.google.android.gms";
    private static final String PROCESS_GMS_UNSTABLE = PACKAGE_GMS + ".unstable";
    private static final String PACKAGE_GPHOTOS = "com.google.android.apps.photos";
    private static final String PACKAGE_SI = "com.google.android.settings.intelligence";
    private static final String SAMSUNG = "com.samsung.";
    private static final String SPOOF_MUSIC_APPS = "persist.sys.disguise_props_for_music_app";

    private static final String TAG = PixelPropsUtils.class.getSimpleName();
    private static final String DEVICE = "ro.product.device";
    private static final boolean DEBUG = false;

    private static final Map<String, Object> propsToChangeGeneric;
    private static final Map<String, Object> propsToChangePixel7Pro;
    private static final Map<String, Object> propsToChangePixel5;
    private static final Map<String, Object> propsToChangePixel2;
    private static final Map<String, Object> propsToChangeMeizu;
    private static final Map<String, ArrayList<String>> propsToKeep;

    // Packages to Spoof as Pixel 7 Pro
    private static final String[] packagesToChangePixel7Pro = {
            "com.amazon.avod.thirdpartyclient",
            "com.android.chrome",
            "com.breel.wallpapers20",
            "com.disney.disneyplus",
            "com.google.android.apps.customization.pixel",
            "com.google.android.apps.emojiwallpaper",
            "com.google.android.apps.privacy.wildlife",
            "com.google.android.apps.subscriptions.red",
            "com.google.android.apps.wallpaper",
            "com.google.android.apps.wallpaper.pixel",
            "com.google.android.wallpaper.effects",
            "com.google.pixel.livewallpaper",
            "com.microsoft.android.smsorganizer",
            "com.nhs.online.nhsonline",
            "com.nothing.smartcenter",
            "in.startv.hotstar"
    };

    // Packages to Spoof as Pixel 2
    private static final String[] packagesToChangePixel2 = {
            "com.snapchat.android"
    };

    private static final String[] customGoogleCameraPackages = {
            "com.google.android.MTCL83",
            "com.google.android.UltraCVM",
            "com.google.android.apps.cameralite"
    };

    // Packages to Keep with original device
    private static final String[] packagesToKeep = {
            PACKAGE_FINSKY,
            PACKAGE_GMS,
            PACKAGE_GPHOTOS,
            "com.google.android.apps.motionsense.bridge",
            "com.google.android.apps.pixelmigrate",
            "com.google.android.apps.recorder",
            "com.google.android.apps.restore",
            "com.google.android.apps.tachyon",
            "com.google.android.apps.tycho",
            "com.google.android.apps.wearables.maestro.companion",
            "com.google.android.apps.youtube.kids",
            "com.google.android.apps.youtube.music",
            "com.google.android.as",
            "com.google.android.dialer",
            "com.google.android.euicc",
            "com.google.android.setupwizard",
            "com.google.android.youtube",
            "com.google.ar.core",
            "com.google.oslo"
    };

    // Packages to Spoof as Meizu
    private static final String[] packagesToChangeMeizu = {
            "com.netease.cloudmusic",
            "com.tencent.qqmusic",
            "com.kugou.android",
            "com.kugou.android.lite",
            "cmccwm.mobilemusic",
            "cn.kuwo.player",
            "com.meizu.media.music"
    };

    // Codenames for currently supported Pixels by Google
    private static final String[] pixelCodenames = {
            "felix",
            "tangorpro",
            "lynx",
            "cheetah",
            "panther",
            "bluejay",
            "oriole",
            "raven",
            "barbet",
            "redfin",
            "bramble",
            "sunfish",
            "coral",
            "flame",
            "bonito",
            "sargo",
            "crosshatch",
            "blueline",
            "taimen",
            "walleye"
    };

    static {
        propsToKeep = new HashMap<>();
        propsToKeep.put(PACKAGE_SI, new ArrayList<>(Collections.singletonList("FINGERPRINT")));
        propsToChangeGeneric = new HashMap<>();
        propsToChangeGeneric.put("TYPE", "user");
        propsToChangeGeneric.put("TAGS", "release-keys");
        propsToChangePixel7Pro = new HashMap<>();
        propsToChangePixel7Pro.put("BRAND", "google");
        propsToChangePixel7Pro.put("MANUFACTURER", "Google");
        propsToChangePixel7Pro.put("DEVICE", "cheetah");
        propsToChangePixel7Pro.put("PRODUCT", "cheetah");
        propsToChangePixel7Pro.put("HARDWARE", "cheetah");
        propsToChangePixel7Pro.put("MODEL", "Pixel 7 Pro");
        propsToChangePixel7Pro.put("ID", "TQ3A.230901.001");
        propsToChangePixel7Pro.put("FINGERPRINT", "google/cheetah/cheetah:13/TQ3A.230901.001/10750268:user/release-keys");
        propsToChangePixel5 = new HashMap<>();
        propsToChangePixel5.put("BRAND", "google");
        propsToChangePixel5.put("MANUFACTURER", "Google");
        propsToChangePixel5.put("DEVICE", "redfin");
        propsToChangePixel5.put("PRODUCT", "redfin");
        propsToChangePixel5.put("HARDWARE", "redfin");
        propsToChangePixel5.put("MODEL", "Pixel 5");
        propsToChangePixel5.put("ID", "TQ3A.230901.001");
        propsToChangePixel5.put("FINGERPRINT", "google/redfin/redfin:13/TQ3A.230901.001/10750268:user/release-keys");
        propsToChangePixel2 = new HashMap<>();
        propsToChangePixel2.put("BRAND", "google");
        propsToChangePixel2.put("MANUFACTURER", "Google");
        propsToChangePixel2.put("DEVICE", "walleye");
        propsToChangePixel2.put("PRODUCT", "walleye");
        propsToChangePixel2.put("HARDWARE", "walleye");
        propsToChangePixel2.put("MODEL", "Pixel 2");
        propsToChangePixel2.put("ID", "OPM1.171019.011");
        propsToChangePixel2.put("FINGERPRINT", "google/walleye/walleye:8.1.0/OPM1.171019.011/4448085:user/release-keys");
        propsToChangeMeizu = new HashMap<>();
        propsToChangeMeizu.put("BRAND", "meizu");
        propsToChangeMeizu.put("MANUFACTURER", "Meizu");
        propsToChangeMeizu.put("DEVICE", "m1892");
        propsToChangeMeizu.put("DISPLAY", "Flyme");
        propsToChangeMeizu.put("PRODUCT", "meizu_16thPlus_CN");
        propsToChangeMeizu.put("MODEL", "meizu 16th Plus");
    }

    private static boolean isGoogleCameraPackage(String packageName) {
        return packageName.startsWith("com.google.android.GoogleCamera")
            || Arrays.asList(customGoogleCameraPackages).contains(packageName);
    }

    public static void setProps(Context context) {
        final String packageName = context.getPackageName();
        final String processName = Application.getProcessName();

        propsToChangeGeneric.forEach((k, v) -> setPropValue(k, v));
        if (packageName == null || packageName.isEmpty()) {
            return;
        }
        if (Arrays.asList(packagesToKeep).contains(packageName)) {
            return;
        }
        if (isGoogleCameraPackage(packageName)) {
            return;
        }
        Map<String, Object> propsToChange = new HashMap<>();
        if (packageName.startsWith("com.google.")
                || packageName.startsWith(SAMSUNG)
                || Arrays.asList(packagesToChangePixel2).contains(packageName)
                || Arrays.asList(packagesToChangePixel7Pro).contains(packageName)) {

            boolean isPixelDevice = Arrays.asList(pixelCodenames).contains(SystemProperties.get(DEVICE));
            if (isPixelDevice) {
                return;
            } else if (Arrays.asList(packagesToChangePixel2).contains(packageName)) {
                propsToChange.putAll(propsToChangePixel2);
            } else if (Arrays.asList(packagesToChangePixel7Pro).contains(packageName)) {
                propsToChange.putAll(propsToChangePixel7Pro);
            } else {
                propsToChange.putAll(propsToChangePixel5);
            }
        } else if ((SystemProperties.getBoolean(SPOOF_MUSIC_APPS, false)) &&
                (Arrays.asList(packagesToChangeMeizu).contains(packageName))) {
            propsToChange.putAll(propsToChangeMeizu);
        }
        dlog("Defining props for: " + packageName);
        for (Map.Entry<String, Object> prop : propsToChange.entrySet()) {
            String key = prop.getKey();
            Object value = prop.getValue();
            if (propsToKeep.containsKey(packageName) && propsToKeep.get(packageName).contains(key)) {
                dlog("Not defining " + key + " prop for: " + packageName);
                continue;
            }
            dlog("Defining " + key + " prop for: " + packageName);
            setPropValue(key, value);
        }
        // Set proper indexing fingerprint
        if (packageName.equals(PACKAGE_SI)) {
            setPropValue("FINGERPRINT", String.valueOf(Build.TIME));
            return;
        }
    }

    private static void setPropValue(String key, Object value) {
        try {
            dlog("Defining prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }

    public static void dlog(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}
