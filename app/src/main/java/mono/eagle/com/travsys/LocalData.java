package mono.eagle.com.travsys;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalData {

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void add(Context ctx, String name, String content) {
        SharedPreferences editor = getSharedPreferences(ctx);
        editor
                .edit()
                .putString(name, content)
                .apply();
    }

    public static void remove(Context ctx, String name) {
        SharedPreferences editor = getSharedPreferences(ctx);
        editor
                .edit()
                .remove(name)
                .apply();
    }

    public static String get(Context ctx, String name) {
        return getSharedPreferences(ctx).getString(name, "");
    }
}
