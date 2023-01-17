package melbet.malbet.hispone.utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {
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
