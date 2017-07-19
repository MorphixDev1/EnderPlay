package br.com.endcraft.me.endcraft.Managers;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by JonasXPX on 06.jul.2017.
 */

public class GetOnlineUsers extends AsyncTask<String, Void, JSONObject>{

    public static final String URL_FOR_CHECK = "https://www.endcraft.com.br/data/data.php?getusers=0";
    public static boolean inUpdated = false;

    public JSONObject doInBackground(@Nullable String ... obj){
        try {
            inUpdated = true;
            URL url = new URL(URL_FOR_CHECK);
            System.out.println("CONECTADO...");
            URLConnection c = url.openConnection();
            InputStream in = c.getInputStream();
            String out = IOUtils.toString(in, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(out);
            inUpdated = false;
            return json;
        }catch (IOException e){e.printStackTrace();} catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return new JSONObject("{\"null\": \"null\"}");
        }catch (JSONException e){}
        return null;
    }

    public static String formatUsers(JSONObject e) throws JSONException{
        StringBuilder sb = new StringBuilder();
        JSONArray players = e.getJSONArray("players");
        sb.append("Total: " + players.length() + "\n");
        for(int x=0; x<players.length();x++){
            sb.append(players.get(x));
            sb.append("\n");
        }
        return sb.toString();
    }
}
