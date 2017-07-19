package br.com.endcraft.me.endcraft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by JonasXPX on 18.jul.2017.
 */
public class Descview extends AppCompatActivity {

    private CharSequence nome;
    private CharSequence idioma;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewinfo);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            nome = b.getCharSequence("movie");
            idioma = b.getCharSequence("idioma");
        }
        text = (TextView) findViewById(R.id.viewinfo);
        text.append(nome);

    }


}
