package br.com.endcraft.me.endcraft.Managers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.endcraft.me.endcraft.Filmes;
import br.com.endcraft.me.endcraft.R;
import br.com.endcraft.me.endcraft.ShowSerie;
import br.com.endcraft.me.endcraft.net.ThreadImage;

/**
 * Created by JonasXPX on 21.jul.2017.
 */

public class AdapterCustomSeries extends BaseAdapter {

    private List<Series> seriesList = null;
    private final Activity activity;

    public AdapterCustomSeries(List<Series> seriesList, Activity activity) {
        this.seriesList = seriesList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return seriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return seriesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.filmes, parent, false);
        final Series series = seriesList.get(position);
        TextView nome = (TextView) view.findViewById(R.id.filme_nome);
        ImageView img = (ImageView) view.findViewById(R.id.item_imagem);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filmes.current_serie = series;
                activity.startActivity(new Intent(activity, ShowSerie.class));
            }
        });
        nome.setText(series.getName());
        new Thread(new ThreadImage(series.getUrlImg(), img, activity)).start();
        Log.d("DEG", "loaded" + position);
        return view;
    }
}
