package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.endcraft.me.endcraft.Managers.DownloadImage;


/**
 * Created by JonasXPX on 18.jul.2017.
 */
public class Descview extends AppCompatActivity {

    private CharSequence nome;
    private CharSequence idioma;
    private TextView text;
    private Movie movie;
    private long seek;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filme_desc);
        overridePendingTransition(R.anim.side_in, android.support.v7.appcompat.R.anim.abc_fade_out);
        movie = (Movie) this.getIntent().getSerializableExtra("movie");
        seek = this.getIntent().getLongExtra("seek", 0);
        activity = this;
        TextView desc = (TextView) findViewById(R.id.descricao);
        TextView idioma = (TextView) findViewById(R.id.idioma);
        TextView nome = (TextView) findViewById(R.id.movie_name);
        ImageView play_button = (ImageView) findViewById(R.id.play_button);
        ImageView background_movie = (ImageView) findViewById(R.id.background_movie);
        String banner = movie.getBannerLink();
        if(!banner.equalsIgnoreCase(""))
            new DownloadImage(background_movie, this).execute(banner);

        play_button.setOnClickListener(onClick());
        if(movie != null) {
            nome.setText(movie.getNome());
            idioma.setText(movie.getIdioma());
            desc.setText(movie.getDesc());
            desc.setMovementMethod(new ScrollingMovementMethod());
        } else {
            Toast.makeText(this, "MOVIE IS NULL", Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filmes.openVideo(movie.getLink(), seek, movie.getNome(), activity, movie, null);
            }
        };
    }


}