package br.com.endcraft.me.endcraft.net;

import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by JonasXPX on 30.jul.2017.
 */

public class GetMovieInformation extends AsyncTask<String, Void, String[]> {

    private final TextView year_t;
    private final TextView directors_t;
    private String year = "";
    private String directors = "";

    private static final String URL_TO_GET = "http://essearch.allocine.net/br/autocomplete?geo2=293139&q=";

    public GetMovieInformation(TextView year, TextView directors) {
        this.year_t = year;
        this.directors_t = directors;
    }

    @Override
    protected String[] doInBackground(String... params) {
        return getMovieInformation(params[0]);
    }

    public String[] getMovieInformation(String name) {

        String complete = null;
        try {
            complete = URL_TO_GET + URLEncoder.encode(name.replaceAll("([\\[\\]]|1080p|720p|/|Legendado)", ""), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONArray data = new JSONArray(IOUtils.toString(new URL(complete).openStream(), "UTF-8"));
            if(data.length() == 0){
                return null;
            }
            JSONObject jsonObject = data.getJSONObject(0);
            JSONArray metadata = jsonObject.getJSONArray("metadata");
            for(int x = 0; x< metadata.length(); x++){
                if(metadata.getJSONObject(x).getString("property").equalsIgnoreCase("productionyear")){
                    year = metadata.getJSONObject(x).getString("value");
                } else if (metadata.getJSONObject(x).getString("property").equalsIgnoreCase("director")){
                    directors += metadata.getJSONObject(x).getString("value") + ", ";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        directors = directors.replaceAll(",.$", "").trim();
        return new String[]{year, directors};
    }


    @Override
    protected void onPostExecute(String[] strings) {
        year_t.setText(strings[0]);
        directors_t.setText(strings[1]);
        super.onPostExecute(strings);
    }
}
