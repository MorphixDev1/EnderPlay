package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomSeries;
import br.com.endcraft.me.endcraft.Managers.Series;

/**
 * Created by JonasXPX on 21.jul.2017.
 */

public class LoadSeries extends AsyncTask<String, Void, List<Series>> {


    private Activity activity;
    private GridView gv;

    public LoadSeries(Activity activity, GridView gv) {
        this.activity = activity;
        this.gv = gv;
    }

    @Override
    protected List<Series> doInBackground(String... params) {
        List<Series> seriesList = new ArrayList<>();
        try{
            URL url = new URL(params[0]);
            URLConnection c = url.openConnection();
            c.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            c.connect();
            JSONArray series = new JSONArray(IOUtils.toString(c.getInputStream(), "UTF-8"));
            for(int x = 0; x < series.length(); x++){
                Series seriesData;
                JSONObject jo = series.getJSONObject(x);
                seriesData = new Series(jo.getString("nome"));
                seriesData.setUrlImg(jo.getString("img"));
                TreeMap<Integer, List<Series.Episodio>> ep = new TreeMap<>();
                JSONArray tempEp = jo.getJSONArray("temporadas");
                for(int y = 0; y < tempEp.length(); y++){
                    JSONObject s = tempEp.getJSONObject(y);
                    List<Series.Episodio> episodios = ep.get(s.getInt("temporada"));
                    if(episodios == null){
                        ep.put(s.getInt("temporada"), episodios = new ArrayList<>());
                    }
                    String link = s.getString("link");
                    if(!link.matches("http[:s].*")){
                        link = "https://storage.bhs1.cloud.ovh.net/v1/AUTH_338f030c60e64960aef70fb593d452f1/ender/" + link;
                    }
                    Series.Episodio e = new Series.Episodio(s.getString("nome"), s.getInt("episodio"), link);
                    Collections.sort(episodios, new Comparator<Series.Episodio>() {
                        @Override
                        public int compare(Series.Episodio o1, Series.Episodio o2) {
                            return o1.getEpNumber() - o2.getEpNumber();
                        }
                    });
                    episodios.add(e);
                }
                seriesData.setTemporadas(ep);
                seriesList.add(seriesData);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return seriesList;
    }

    @Override
    protected void onPostExecute(List<Series> series) {
        Log.d("D", "SIZE: "+series.size());
        AdapterCustomSeries ad = new AdapterCustomSeries(series, this.activity);
        this.gv.setAdapter(ad);
    }
}
