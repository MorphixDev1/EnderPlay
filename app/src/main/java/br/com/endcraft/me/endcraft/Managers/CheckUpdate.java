package br.com.endcraft.me.endcraft.Managers;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by JonasXPX on 20.jul.2017.
 */

public class CheckUpdate extends AsyncTask<String, Void, Update> {

    @Override
    protected Update doInBackground(String... params) {
        String version = params[0];

        Update update = new Update(version);

        try {
            URL url = new URL("https://ender.tk/filme/data.php?checkversion=0");
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            connection.connect();
            String newVersion = IOUtils.toString(connection.getInputStream(), "UTF-8");
            if(!update.getVersion().equalsIgnoreCase(newVersion)){
                update.setNewVersion(true);
            } else {
                update.setNewVersion(false);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return update;
    }
}
