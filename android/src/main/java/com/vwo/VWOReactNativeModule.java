package com.vwo;

import android.content.Context;
import androidx.annotation.NonNull;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.vwo.RNUtils.Utils;
import com.vwo.mobile.Initializer;
import com.vwo.mobile.VWO;
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
    private static final String LOG_TAG = "VWO_LOG";

    private final ActivityLifecycleListener listener = new ActivityLifecycleListener();

    private static final String VWO_NAME = "VWO";

    private static final String OPT_OUT = "optOut";
    private static final String DISABLE_PREVIEW = "disablePreview";
    private static final String CUSTOM_DIMENSION_KEY = "customDimensionKey";
    private static final String CUSTOM_DIMENSION_VALUE = "customDimensionValue";
    private static final String CUSTOM_VARIABLES = "customVariables";
    private Context mContext;

    public VWOReactNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
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
        return VWO.with(getContext(), apiKey);
    }

    @ReactMethod
    public void setLogLevel(int logLevel) {
        VWOLog.setLogLevel(logLevel);
    }

    @ReactMethod
    public void launch(@NonNull String apiKey, @Nullable ReadableMap config,
                       @Nullable final Promise promise) {
        initializer(apiKey).config(getConfigFromMap(config)).launch(new VWOStatusListener() {

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

    @NonNull
    private VWOConfig getConfigFromMap(@Nullable ReadableMap readableMap) {
        VWOConfig.Builder vwoConfigBuilder = new VWOConfig.Builder();
        vwoConfigBuilder.setLifecycleListener(listener);

        if (readableMap != null) {
            if (readableMap.hasKey(OPT_OUT) && readableMap.getType(CUSTOM_VARIABLES) != ReadableType.Null) {
                try {
                    vwoConfigBuilder.setOptOut(readableMap.getBoolean(OPT_OUT));
                } catch (Exception exception) {
                    VWOLog.w(LOG_TAG, "optOut must be a Boolean", false);
                }
            }

            if (readableMap.hasKey(DISABLE_PREVIEW) && readableMap.getType(CUSTOM_VARIABLES) != ReadableType.Null) {
                try {
                    if (readableMap.getBoolean(DISABLE_PREVIEW)) {
                        vwoConfigBuilder.disablePreview();
                    }
                } catch (Exception exception) {
                    VWOLog.w(LOG_TAG, "disablePreview must be a Boolean", false);
                }
            }

            if (readableMap.hasKey(CUSTOM_DIMENSION_KEY) && readableMap.hasKey(CUSTOM_DIMENSION_VALUE)) {
                try {
                    if (readableMap.getString(CUSTOM_DIMENSION_KEY) != null
                            && !readableMap.getString(CUSTOM_DIMENSION_KEY).equals("")
                            && readableMap.getString(CUSTOM_DIMENSION_VALUE) != null
                            && !readableMap.getString(CUSTOM_DIMENSION_VALUE).equals("")) {
                        vwoConfigBuilder.setCustomDimension(readableMap.getString(CUSTOM_DIMENSION_KEY), readableMap.getString((CUSTOM_DIMENSION_VALUE)));
                    }
                } catch (Exception exception) {
                    VWOLog.w(LOG_TAG, "CustomDimensionKey and CustomDimensionValue should not be null or an empty string", false);
                }
            }

            if (readableMap.hasKey(CUSTOM_VARIABLES) && readableMap.getType(CUSTOM_VARIABLES) != ReadableType.Null) {
                try {
                    vwoConfigBuilder.setCustomVariables(Utils.toHashMap(readableMap.getMap(CUSTOM_VARIABLES)));
                } catch (Exception exception) {
                    VWOLog.w(LOG_TAG, "customVariables must be a javascript object", false);
                }
            }
        }

        return vwoConfigBuilder.build();
    }

    @ReactMethod
    public void intForKey(@NonNull String key, int defaultValue, @Nullable Promise promise) {
        try {
            if (promise != null) {
                promise.resolve(VWO.getIntegerForKey(key, defaultValue));
            }
        } catch (Exception exception) {
            promise.resolve(defaultValue);
        }

    }

    @ReactMethod
    public void stringForKey(@NonNull String key, @Nullable String defaultValue, @Nullable Promise promise) {
        try {
            if (promise != null) {
                promise.resolve(VWO.getStringForKey(key, defaultValue));
            }
        } catch (Exception exception) {
            promise.resolve(defaultValue);
        }
    }

    @ReactMethod
    public void boolForKey(@NonNull String key, boolean defaultValue, @Nullable Promise promise) {
        try {
            if (promise != null) {
                promise.resolve(VWO.getBooleanForKey(key, defaultValue));
            }
        } catch (Exception exception) {
            promise.resolve(defaultValue);
        }
    }

    @ReactMethod
    public void floatForKey(@NonNull String key, double defaultValue, @Nullable Promise promise) {
        try {
            if (promise != null) {
                promise.resolve(VWO.getDoubleForKey(key, defaultValue));
            }
        } catch (Exception exception) {
            promise.resolve(defaultValue);
        }
    }

    @ReactMethod
    public void variationNameForTestKey(@NonNull String testKey, @Nullable Promise promise) {
        try {
            if (promise != null) {
                promise.resolve(VWO.getVariationNameForTestKey(testKey));
            }
        } catch (Exception exception) {
            promise.reject(exception);
        }
    }


    @ReactMethod
    public void __objectForKey__(@NonNull String key, @Nullable Promise promise) {
        Object retrievedObject = VWO.getObjectForKey(key, null);
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
        VWO.trackConversion(goal);
    }

    @ReactMethod
    public void trackConversionWithValue(@NonNull String goal, Double value) {
        VWO.trackConversion(goal, value);
    }

    @ReactMethod
    public void pushCustomDimension(@NonNull String customDimensionKey, @NonNull String customDimensionValue) {
        VWO.pushCustomDimension(customDimensionKey, customDimensionValue);
    }

    @ReactMethod
    public void setCustomVariable(@NonNull String key, @NonNull String value) {
        VWO.setCustomVariable(key, value);
    }

    @ReactMethod
    public void version(@NonNull Promise promise) {
        String version = VWO.version();
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
        constants.put("configOptOut", OPT_OUT);
        constants.put("configDisablePreview", DISABLE_PREVIEW);
        constants.put("configCustomVariables", CUSTOM_VARIABLES);
        return constants;
    }
}