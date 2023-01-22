package bilyoner.vionter;

import static bilyoner.vionter.utils.AndroidUtils.hasSimCard;
import static bilyoner.vionter.utils.AndroidUtils.isEmulator;
import static bilyoner.vionter.utils.AndroidUtils.isGoogleDevice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import bilyoner.vionter.utils.BiFunctionAPI21;
import bilyoner.vionter.utils.WebUtils;


public class WelcomeActivity extends AppCompatActivity {
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
            if (!hasInternetConnection()) {
                runOnUiThread(() -> handleInternetDown(() -> start(pref)));
                return;
            }
            start(pref);
        });
    }

    boolean hasInternetConnection() {
        return WebUtils.inetProbe("http://www.google.com/");
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
                if (!hasInternetConnection())
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

                if (!hasInternetConnection())
                    runOnUiThread(enableButton);
                else
                    tryWithFirebaseConfig(x -> runOnUiThread(enableButton));
            });
        });
    }

    private void switchActivity(String url) {
        Intent intent;
        if (url.isEmpty() || isGoogleDevice() || !hasSimCard(this) || isEmulator(this)) {
            intent = new Intent(WelcomeActivity.this, QuizChooseActivity.class);
            Log.d(TAG, "plug");
        } else {
            intent = new Intent(WelcomeActivity.this, WebViewActivity.class);
            Log.d(TAG, "web view");
            intent.putExtra("url", url);
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

}