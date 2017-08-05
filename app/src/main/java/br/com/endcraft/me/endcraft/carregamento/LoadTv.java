package br.com.endcraft.me.endcraft.carregamento;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomTV;
import br.com.endcraft.me.endcraft.OnlineTV;

/**
 * Created by JonasXPX on 31.jul.2017.
 */

public class LoadTv extends AsyncTask<String, Void, List<OnlineTV>>{

    private Activity activity;
    private GridView gridview;

    public LoadTv(Activity activity, GridView gridview) {
        this.activity = activity;
        this.gridview = gridview;
    }

    @Override
    protected List<OnlineTV> doInBackground(String... params) {
        List<OnlineTV> tvs = new ArrayList<>();
       try {
           URL url = new URL(params[0]);
           URLConnection c = url.openConnection();
           c.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
           c.connect();
           JSONArray json = new JSONArray(IOUtils.toString(c.getInputStream(), "UTF-8").replaceFirst("\\W", ""));
           OnlineTV tv = null;
           for (int i = 0; i < json.length(); i++) {
               JSONObject t = json.getJSONObject(i);
               tv = new OnlineTV(t.getString("name"));
               tv.setImageUrl(t.getString("image"));
               tv.setUrl(t.getString("url"));
               tvs.add(tv);
           }
       }catch (Exception e){
           e.printStackTrace();
           //Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show();
       }
        return tvs;
    }


    @Override
    protected void onPostExecute(List<OnlineTV> onlineTVs) {
        AdapterCustomTV ad = new AdapterCustomTV(onlineTVs, activity);
        gridview.setAdapter(ad);
        super.onPostExecute(onlineTVs);

    }
}
