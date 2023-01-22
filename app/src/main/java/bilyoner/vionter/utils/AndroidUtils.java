package bilyoner.vionter.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.viewbinding.BuildConfig;

import java.util.Locale;



public class AndroidUtils {
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr.getSimState() != TelephonyManager.SIM_STATE_ABSENT;
    }

    public static boolean isGoogleDevice() {
        return Build.BRAND.contains("google");
    }

    //from firebase-android-sdk
    public static boolean isEmulator(Context context) {
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
}
