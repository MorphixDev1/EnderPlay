package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by JonasXPX on 13.set.2017.
 */

public class Pedido {

    private TextView name;
    private TextView email;
    private TextView comment;
    private TextView ref;
    private AlertDialog dialog;

    public Pedido(){
    }

    public void init(AlertDialog dialog){
        this.dialog = dialog;
        email = ((TextView)dialog.findViewById(R.id.pedido_mail));
        comment = ((TextView)dialog.findViewById(R.id.pedido_obs));
        ref = ((TextView)dialog.findViewById(R.id.pedido_ref));
        name = ((TextView)dialog.findViewById(R.id.pedido_name));
    }

    public AlertDialog createDialog(final Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(((Activity)context).getLayoutInflater().inflate(R.layout.pedido_dialog, null));
        dialog.setPositiveButton(R.string.pedido_enviar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                send();
                dialog.dismiss();
            }
        });
        AlertDialog d = dialog.create();
        this.dialog = d;
        return d;
    }

    public void send(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String mail = "mailto:endcraftpvp@hotmail.com?subject=[EnderPlay] Pedido:"
                + this.name.getText() + "\n" + "&body=Nome do filme: " + this.name.getText()
                + "\n" + "Referência: " + this.ref.getText()
                + "\n" + "Comentários: " + this.comment.getText()
                + "\n" + "Meu e-mail: " + this.email.getText();
        intent.setData(Uri.parse(mail));
        dialog.getContext().startActivity(intent);
    }

}
