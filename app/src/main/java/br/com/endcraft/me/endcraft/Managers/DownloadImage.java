package br.com.endcraft.me.endcraft.Managers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.data.BitmapTeleporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class DownloadImage  extends AsyncTask<String, Void, Bitmap> {
    private final Activity activity;
    private ImageView img;
    private MessageDigest md;

    public DownloadImage(ImageView img, Activity activity) {
        this.img = img;
        this.activity = activity;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        boolean mat = url.matches("(^http|^https).*");
        String hash = toHex(md.digest(url.getBytes()));
        File folder = new File(activity.getCacheDir(), "img/");
        folder.mkdirs();
        File file = new File(activity.getCacheDir(), "img/" + hash + ".png");
        //Log.d("HASH", hash);
        if(file.exists()){
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        if(!mat){
            Log.d("WARN", "INVALID URL");
            url = "https://ender.tk/filme/" + url;
        }
        Bitmap bm = null;
        FileOutputStream out = null;
        try{
            Log.d("INFO", "TRY DOWNLOAD IMG: " + url.replaceFirst("http:", "https:"));
            InputStream in = new URL("https://ender.tk/filme/resize.php?URL=" + url.replaceFirst("http:", "https:")).openStream();
            bm = BitmapFactory.decodeStream(in);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out = (new FileOutputStream(new File(activity.getCacheDir(), "img/" + hash + ".png"))));
            Log.d("INFO", "Downloaded image: " + url);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
           try{if(out!=null)out.close();}catch (IOException e){};
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }

    private String toHex(byte[] byteData){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
