package com.vwo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.vwo.RNUtils.Utils;
import com.vwo.mobile.Initializer;
import com.vwo.mobile.VWOConfig;
import com.vwo.mobile.events.VWOStatusListener;
import com.vwo.mobile.listeners.ActivityLifecycleListener;
import com.vwo.mobile.utils.VWOLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class VWOReactNativeModule extends ReactContextBaseJavaModule {

    public static final String VWO_NAME = "VWO";

    private Context mContext;
    private VWOConfig mConfig;

    public VWOReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
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

    private Initializer initializer(@NonNull String apiKey) {
        return com.vwo.mobile.VWO.with(getContext(), apiKey);
    }

    @ReactMethod
    public void setLogLevel(int logLevel) {
        VWOLog.setLogLevel(logLevel);
    }

    @ReactMethod
    public void launch(@NonNull String apiKey,
                       @Nullable final Promise promise) {
        initializer(apiKey).config(mConfig).launch(new VWOStatusListener() {

            @Override
            public void onVWOLoaded() {
                if (promise != null) {
                    promise.resolve(null);
                }
            }

            @Override
            public void onVWOLoadFailure(String message) {
                if (promise != null) {
                    promise.reject("VWO_LAUNCH_ERR", message, new Exception(message));
                }
            }
        });
    }

    @ReactMethod
    @Nullable
    public void variationForKey(@NonNull String key, @Nullable Promise promise) {
        Object retrievedObject = com.vwo.mobile.VWO.getVariationForKey(key);
        if (promise != null) {
            if (retrievedObject == null) {
                promise.resolve(null);
            } else {
                if (retrievedObject instanceof JSONObject) {
                    try {
                        promise.resolve(Utils.convertJsonToMap((JSONObject) retrievedObject));
                    } catch (JSONException exception) {
                        VWOLog.e(VWOLog.DATA_LOGS, exception, false, false);
                        promise.reject(exception);
                    }
                } else if (retrievedObject instanceof JSONArray) {
                    try {
                        promise.resolve(Utils.convertJsonToArray((JSONArray) retrievedObject));
                    } catch (JSONException exception) {
                        VWOLog.e(VWOLog.DATA_LOGS, exception, false, false);
                        promise.reject(exception);
                    }
                } else {
                    promise.resolve(retrievedObject);
                }
            }
        }
    }

    @ReactMethod
    public void trackConversion(@NonNull String goal) {
        com.vwo.mobile.VWO.trackConversion(goal);
    }

    @ReactMethod
    public void trackConversionWithValue(@NonNull String goal, Double value) {
        com.vwo.mobile.VWO.trackConversion(goal, value);
    }

    @ReactMethod
    public void setCustomVariable(@NonNull String key, @NonNull String value) {
        com.vwo.mobile.VWO.setCustomVariable(key, value);
    }

    @ReactMethod
    public void setOptOut(boolean optOut) {
        com.vwo.mobile.VWO.setOptOut(optOut);
    }

    @ReactMethod
    public void version(@NonNull Promise promise) {
        String version = com.vwo.mobile.VWO.version();
        promise.resolve(version);
    }

    /**
     * @return a map of constants this module exports to JS. Supports JSON types.
     */
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("logLevelDebug", 800);
        constants.put("logLevelInfo", 700);
        constants.put("logLevelWarning", 900);
        constants.put("logLevelError", 1000);
        constants.put("logLevelOff", Integer.MAX_VALUE);
        return constants;
    }
}