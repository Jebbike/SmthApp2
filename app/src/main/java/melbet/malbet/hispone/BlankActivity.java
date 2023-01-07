package melbet.malbet.hispone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Locale;

import melbet.malbet.hispone.utils.BiFunctionAPI21;

public class BlankActivity extends AppCompatActivity {
    private static final String URL_KEY = "url";
    private static final String URL_KEY_DEFAULT = "";
    private static final String TAG = "NavigateActivity";
    public FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        AsyncTask.execute(() -> {
            if (!App.inetProbe()) {
                runOnUiThread(() -> handleInternetDown(() -> start(pref)));
                return;
            }
            start(pref);
        });
    }

    void start(SharedPreferences pref) {
        if (!tryWithLocalSettings(pref)) {
            setupFirebase();
            tryWithFirebaseConfig(onFail -> handleFirebaseError());
        }
    }

    private boolean tryWithLocalSettings(SharedPreferences pref) {
        String url = pref.getString(URL_KEY, URL_KEY_DEFAULT);

        if (url.isEmpty())
            return false;

        switchActivity(url);

        return true;
    }

    void setupFirebase() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
    }

    private void tryWithFirebaseConfig(OnFailureListener e) {
        remoteConfig.fetchAndActivate()
                .addOnSuccessListener(__ -> withFirebaseConfig())
                .addOnFailureListener(e);
    }

    void withFirebaseConfig() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().remove(URL_KEY).apply();
        String url = pref.getString(URL_KEY, remoteConfig.getValue(URL_KEY).asString());
        switchActivity(url);
    }

    void handleInternetDown(Runnable onConnectionUp) {
        setErrorScreen(R.string.no_inet, R.string.reload, (textView, button) -> __ -> {
            button.setEnabled(false);
            AsyncTask.execute(() -> {
                if (!App.inetProbe())
                    runOnUiThread(() -> button.setEnabled(true));
                else
                    onConnectionUp.run();
            });
        });
    }

    void handleFirebaseError() {
        setErrorScreen(R.string.firebase_error, R.string.reload, (textView, button) -> __ -> {
            button.setEnabled(false);
            AsyncTask.execute(() -> {
                Runnable enableButton = () -> button.setEnabled(true);

                if (!App.inetProbe())
                    runOnUiThread(enableButton);
                else
                    tryWithFirebaseConfig(x -> runOnUiThread(enableButton));
            });
        });
    }

    private void switchActivity(String url) {
        Intent intent;
        if (url.isEmpty() || isGoogleDevice() || !hasSimCard() || isEmulator(this)) {
            intent = new Intent(BlankActivity.this, PlugActivity.class);
            Log.d(TAG, "plug");
        } else {
            intent = new Intent(BlankActivity.this, WebViewActivity.class);
            Log.d(TAG, "web view");
            intent.putExtra("url", "https://lichess.org/");
        }

        startActivity(intent);
    }

    void setErrorScreen(int errorDetailsText, int buttonText,
                        BiFunctionAPI21<TextView, Button, View.OnClickListener> contentProvider) {
        setContentView(R.layout.error_layout);

        TextView errorDetails = findViewById(R.id.error_details);
        Button errorButton = findViewById(R.id.error_button);

        errorButton.setVisibility(View.VISIBLE);
        errorDetails.setText(errorDetailsText);
        errorButton.setText(buttonText);

        errorButton.setOnClickListener(contentProvider.apply(errorDetails, errorButton));
    }

    public boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr.getSimState() != TelephonyManager.SIM_STATE_ABSENT;
    }

    public boolean isGoogleDevice() {
        return Build.BRAND.contains("google");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    //from firebase-android-sdk
    public boolean isEmulator(Context context) {
        if (BuildConfig.DEBUG)
            return false;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        boolean result = androidId == null
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
                || Build.PRODUCT.contains("simulator")
                || Build.BOARD.toLowerCase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.toLowerCase(Locale.getDefault()).contains("nox")
                || Build.HARDWARE.toLowerCase(Locale.getDefault()).contains("nox")
                || Build.HARDWARE.toLowerCase(Locale.getDefault()).contains("nox");

        if (result)
            return true;

        result = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"));

        return result;
    }

    public App getApp() {
        return (App) getApplication();
    }
}