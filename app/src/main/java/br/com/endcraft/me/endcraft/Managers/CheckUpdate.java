package br.com.endcraft.me.endcraft.Managers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;

import br.com.endcraft.me.endcraft.R;

/**
 * Created by JonasXPX on 20.jul.2017.
 */

public class CheckUpdate extends AsyncTask<String, Void, Update> {

    private Activity activity;

    public CheckUpdate(Activity activity) {
        Toast.makeText(activity, R.string.check_update, Toast.LENGTH_SHORT).show();
        this.activity = activity;
    }

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
            JSONObject jversion = new JSONObject(newVersion);
            String downloadLink = jversion.getString("download");
            String lastVersion = jversion.getString("lastversion");
            Log.d("UPDATE", lastVersion + " / " + version);
            if(!update.getVersion().equalsIgnoreCase(lastVersion)){
                update.setNewVersion(true);
                update.setDownloadLink(downloadLink);
                update.setLastVersion(lastVersion);
                Log.d("UPDATE", "Has a new version");
            } else {
                update.setNewVersion(false);
                Log.d("UPDATE", "DOESN'T Has a new version");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return update;
    }

    @Override
    protected void onPostExecute(final Update update) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        boolean check = update.hasNewVersion();
        Log.d("DIALOG", "DIALOG WAS CALLED: " + check);
        if(check) {
            builder.setMessage(activity.getString(R.string.new_update_found) + update.getLastVersion()).setTitle(R.string.update_title);
            builder.setPositiveButton(R.string.update_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent open = new Intent(Intent.ACTION_VIEW, Uri.parse(update.getDownloadLink()));
                    activity.startActivity(open);
                }
            });
            builder.create().show();
        } else {
            builder = null;
            Toast.makeText(activity, R.string.alread_update, Toast.LENGTH_SHORT).show();
        }
    }
}
