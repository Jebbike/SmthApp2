package melbet.malbet.hispone;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import melbet.malbet.hispone.plug.Info;


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
    public static boolean inetProbe(String adress) {
        try {
            URL url = new URL(adress);

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