package melbet.malbet.hispone;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    /**
     * Проверяет подключение к интернету
     *
     * @return true если инет есть, false если инета нет или ошибка подклюечения
     */
    public static boolean inetProbe() {
        try {
            URL url = new URL("http://www.google.com/");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}