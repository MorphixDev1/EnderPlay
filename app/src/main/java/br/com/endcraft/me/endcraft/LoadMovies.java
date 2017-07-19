package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class LoadMovies extends AsyncTask<String, Void, List<Movie>> {

    private Activity ab;
    private GridView view;

    public LoadMovies(Activity ab, GridView view) {
        this.ab = ab;
        this.view = view;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        List<Movie> filmes = new ArrayList<>();
        try{
            URL url = new URL(params[0]);
            URLConnection c = url.openConnection();
            c.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            c.connect();
            JSONArray j = new JSONArray(IOUtils.toString(c.getInputStream(), "UTF-8").replaceFirst("\\W", ""));
            Movie m;
            for (int x=0;x<j.length();x++){
                JSONObject data = j.getJSONObject(x);
                m = new Movie();
                m.setImgLink(data.getString("poster"));
                m.setNome(data.getJSONObject("nome").getString("0"));
                m.setIdioma(data.getString("categoriaf"));
                m.setLink(data.getJSONObject("url").getString("0"));
                filmes.add(m);
            }
        }catch (Exception e){
            e.printStackTrace();
            return filmes;
        }

        return filmes;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        AdapterCustomFilmes adapterCustomFilmes = new AdapterCustomFilmes(movies, this.ab);
        view.setAdapter(adapterCustomFilmes);
    }
}
