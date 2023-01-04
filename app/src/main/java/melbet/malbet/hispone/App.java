package melbet.malbet.hispone;

import android.app.Application;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class App extends Application {
    public FirebaseRemoteConfig remoteConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        //config
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
    }

    public FirebaseRemoteConfig getRemoteConfig() {
        return remoteConfig;
    }
}