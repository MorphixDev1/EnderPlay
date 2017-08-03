package br.com.endcraft.me.endcraft.net;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JonasXPX on 30.jul.2017.
 */

public class ThreadImage implements Runnable {

    private MessageDigest md;
    private String url;
    private ImageView img;
    private final Activity activity;

    public ThreadImage(String url, ImageView img, Activity activity) {
        this.url = url;
        this.img = img;
        this.activity = activity;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean mat = url.matches("(^http|^https).*");
        String hash = toHex(md.digest(url.getBytes()));
        File folder = new File(activity.getCacheDir(), "img/");
        folder.mkdirs();
        File file = new File(activity.getCacheDir(), "img/" + hash + ".png");
        if(file.exists()){
            Log.d("WARN", "Image load by cache");
            done(BitmapFactory.decodeFile(file.getAbsolutePath()));
            return;
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
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{if(out!=null)out.close();}catch (IOException e){};
        }
        done(bm);
    }


    public void done(final Bitmap bitmap){
        Log.d("DONE", "Image downloaded");
        img.post(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(bitmap);
            }
        });
    }

    private String toHex(byte[] byteData){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
