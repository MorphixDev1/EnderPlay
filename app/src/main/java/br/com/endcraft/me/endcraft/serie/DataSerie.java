package br.com.endcraft.me.endcraft.serie;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by JonasXPX on 29.jul.2017.
 */

public class DataSerie {

    private Series series;
    private Activity activity;
    private SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "SERIE";
    private SharedPreferences pre;

    public DataSerie(Series series, Activity activity) {
        this.series = series;
        this.activity = activity;
    }

    public void setVisualized(int t, int e, @Nullable long seek){
        pre = activity.getSharedPreferences(PREFS_NAME, 0);
        editor = pre.edit();
        editor.putLong(series.getName() + "_" + t + "-" + e, seek);
        Log.d("DATASERIE", "SET TO: " + t + " -- " + e);
        editor.commit();
    }

    public Long getVisualized(int t, int e){
        pre = activity.getSharedPreferences(PREFS_NAME, 0);
        return pre.getLong(series.getName() + "_" + t + "-" + e, 0);
    }

}
