
package com.vwo;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.vwo.mobile.Initializer;
import com.vwo.mobile.VWOConfig;
import com.vwo.mobile.events.VWOStatusListener;
import com.vwo.mobile.listeners.ActivityLifecycleListener;
import com.vwo.mobile.utils.VWOLog;
import com.vwo.RNUtils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class VWOReactNativeModule extends ReactContextBaseJavaModule {

    public static final String TAG = VWOReactNativeModule.class.getSimpleName();
    public static final String VWO_NAME = "VWO";

    private Context mContext;
    private VWOConfig mConfig;

    public VWOReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        VWOLog.setLogLevel(VWOLog.ALL);
        final ActivityLifecycleListener listener = new ActivityLifecycleListener();
        mConfig = new VWOConfig.Builder().setLifecycleListener(listener).build();
        reactContext.addLifecycleEventListener(new LifecycleEventListener() {

            @Override
            public void onHostResume() {
                listener.onResume();
            }

            @Override
            public void onHostPause() {
                listener.onPause();
            }

            @Override
            public void onHostDestroy() {
                listener.onStop();
            }
        });
        this.mContext = getReactApplicationContext();
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public String getName() {
        return VWO_NAME;
    }

    private Initializer initializer(String apiKey) {
        return com.vwo.mobile.VWO.with(getContext(), apiKey);
    }

    @ReactMethod
    public void setLogLevel(int logLevel) {
        VWOLog.setLogLevel(logLevel);
    }

    @ReactMethod
    public void launchAsynchronouslyWithCallback(String apiKey, final Callback successCallback) {
        initializer(apiKey).config(mConfig).launch(new VWOStatusListener() {

            @Override
            public void onVWOLoaded() {
                successCallback.invoke();
            }

            @Override
            public void onVWOLoadFailure(String message) {

            }
        });
    }

    @ReactMethod
    public void launchSynchronously(String apiKey, long timeout) {
        initializer(apiKey).config(mConfig).launchSynchronously(timeout);
    }

    @ReactMethod
    public void launchAsynchronously(String apiKey) {
        initializer(apiKey).config(mConfig).launch();
    }

    @ReactMethod
    public WritableMap getVariationForKey(String key, Callback callback) {
        WritableMap obj = null;
        try {
            obj = Utils.convertJsonToMap((JSONObject) com.vwo.mobile.VWO.getVariationForKey(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "getVariationForKey: "+obj);
        callback.invoke(obj);
        return obj;
    }

    @ReactMethod
    public WritableMap getVariationForKeyWithDefaultValue(String key, String defaultValue, Callback callback) {
        WritableMap obj = null;
        try {
            Object retrievedObject = com.vwo.mobile.VWO.getVariationForKey(key, defaultValue);
            if (retrievedObject instanceof String) {
                obj = new WritableNativeMap();
                obj.putString(key, (String) retrievedObject);
            } else {
                obj = Utils.convertJsonToMap((JSONObject) retrievedObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callback.invoke(obj);
//        Log.d(TAG, "getVariationForKeyWithDefaultValue: "+obj);
        return obj;
    }

    @ReactMethod
    public void markConversionForGoal(String goal) {
        com.vwo.mobile.VWO.markConversionForGoal(goal);
    }

    @ReactMethod
    public void markConversionForGoalWithValue(String goal, Double value) {
        com.vwo.mobile.VWO.markConversionForGoal(goal, value);
    }

    @ReactMethod
    public void setCustomVariable(String key, String value) {
        com.vwo.mobile.VWO.setCustomVariable(key, value);
    }

    @ReactMethod
    public void launchWithVWOConfig(String apiKey, HashMap<String, String> userSegmentationMapping) {
        initializer(apiKey).config(new VWOConfig
                .Builder()
                .setCustomSegmentationMapping(userSegmentationMapping)
                .build())
                .launch();
    }

    @ReactMethod
    public String getVersion() {
        return com.vwo.mobile.VWO.version();
    }

    /**
     * @return a map of constants this module exports to JS. Supports JSON types.
     */
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("LogLevelDebug", 800);
        constants.put("LogLevelInfo", 700);
        constants.put("LogLevelWarning", 900);
        constants.put("LogLevelError", 1000);
        constants.put("LogLevelOff", Integer.MAX_VALUE);
        return constants;
    }
}