
package com.vwo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.vwo.mobile.Initializer;
import com.vwo.mobile.VWOConfig;
import com.vwo.mobile.events.VWOStatusListener;
import com.vwo.mobile.listeners.ActivityLifecycleListener;
import com.vwo.mobile.utils.VWOLog;

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
    public void launchAsynchronouslyWithCallback(@NonNull String apiKey,
                                                 @Nullable final Callback completionCallback) {
        initializer(apiKey).config(mConfig).launch(new VWOStatusListener() {

            @Override
            public void onVWOLoaded() {
                if(completionCallback != null) {
                    completionCallback.invoke(null, "");
                }
            }

            @Override
            public void onVWOLoadFailure(String message) {
                if(completionCallback != null) {
                    completionCallback.invoke(message);
                }
            }
        });
    }

//    @ReactMethod(isBlockingSynchronousMethod = true)
//    public void launchSynchronously(@NonNull String apiKey) {
//        initializer(apiKey).config(mConfig).launchSynchronously(3000);
//    }

    @ReactMethod
    public void launchAsynchronously(@NonNull String apiKey) {
        initializer(apiKey).config(mConfig).launch();
    }

    @ReactMethod
    @Nullable
    public Object variationForKey(@NonNull String key, @Nullable Callback callback) {
        Object obj;
        obj = com.vwo.mobile.VWO.getVariationForKey(key);
        if(callback != null) {
            callback.invoke(null, obj);
        }
        return obj;
    }

    @ReactMethod
    @NonNull
    public Object variationForKeyWithDefaultValue(@NonNull String key, @NonNull Object defaultValue, @Nullable Callback callback) {
        Object retrievedObject = com.vwo.mobile.VWO.getVariationForKey(key, defaultValue);

        if(callback != null) {
            callback.invoke(null, retrievedObject);
        }
        return retrievedObject;
    }

    @ReactMethod
    public void markConversionForGoal(@NonNull String goal) {
        com.vwo.mobile.VWO.markConversionForGoal(goal);
    }

    @ReactMethod
    public void markConversionForGoalWithValue(@NonNull String goal, Double value) {
        com.vwo.mobile.VWO.markConversionForGoal(goal, value);
    }

    @ReactMethod
    public void setCustomVariable(@NonNull String key, @NonNull String value) {
        com.vwo.mobile.VWO.setCustomVariable(key, value);
    }

    @ReactMethod
    public void launchWithVWOConfig(@NonNull String apiKey, @NonNull HashMap<String, String> userSegmentationMapping) {
        initializer(apiKey).config(new VWOConfig
                .Builder()
                .setCustomSegmentationMapping(userSegmentationMapping)
                .build())
                .launch();
    }

    @ReactMethod
    public String version(@NonNull Callback callback) {
        String version = com.vwo.mobile.VWO.version();
        callback.invoke(null, version);
        return version;
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