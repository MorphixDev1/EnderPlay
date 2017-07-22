package br.com.endcraft.me.endcraft.Managers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import br.com.endcraft.me.endcraft.Filmes;
import br.com.endcraft.me.endcraft.R;

/**
 * Created by JonasXPX on 21.jul.2017.
 */

public class AdapterExpandSeries extends BaseExpandableListAdapter {

    private Series series;
    private Context context;

    public AdapterExpandSeries(Series series, Context context) {
        this.series = series;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return series.getTemporadas().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return series.getTemporadas().get(groupPosition + 1).size();
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("DEBUG", "CALLED: " + groupPosition + " ARRAY SIZE: " + series.getTemporadas().size());
        }
        return -1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return series.getTemporadas().keySet().toArray()[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return series.getTemporadas().get(groupPosition + 1).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        int ep = (int)getGroup(groupPosition);
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_ep, null);
        }
        TextView temporadas = (TextView) view.findViewById(R.id.temporadas);
        temporadas.setText("Episódio " + ep);
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        final Series.Episodio temporada = (Series.Episodio) getChild(groupPosition, childPosition);
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.episodio);
        textView.setText(temporada.getName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filmes.openVideo(temporada.getUrl(), 0, series.getName() + "-" + temporada.getName() + "-" + temporada.getEpNumber());
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
