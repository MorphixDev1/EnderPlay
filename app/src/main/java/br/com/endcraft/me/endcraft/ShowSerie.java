package br.com.endcraft.me.endcraft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import br.com.endcraft.me.endcraft.Managers.AdapterExpandSeries;
import br.com.endcraft.me.endcraft.Managers.Series;
import br.com.endcraft.me.endcraft.net.ThreadImage;

/**
 * Created by JonasXPX on 21.jul.2017.
 */

public class ShowSerie extends AppCompatActivity {

    private Series series;
    private ExpandableListView listView;
    private AdapterExpandSeries listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        series = Filmes.current_serie;
        setContentView(R.layout.series_layout);

        listView = (ExpandableListView)findViewById(R.id.listing);
        listAdapter = new AdapterExpandSeries(series, this);
        listView.setAdapter(listAdapter);
        ImageView img = (ImageView) this.findViewById(R.id.serieImage);

        new Thread(new ThreadImage(series.getUrlImg(), img, this)).start();
    }
}
