package com.gymlazy.tripplanner.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloader {
    public static void downloadImgFromURL(String imgURL) throws IOException {
        URL url = new URL(imgURL);
        Bitmap bmp = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bOutImg = null;
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//        Bitmap bmp = BitmapFactory.decodeStream(connection.getInputStream());

        try{
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        url);
            }

            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while((byteRead = in.read(buffer)) > 0){
                out.write(buffer, 0, byteRead);
            }
            out.close();
        } finally {
            connection.disconnect();
        }

        bOutImg = out.toByteArray();
        bmp = BitmapFactory.decodeByteArray(bOutImg, 0 , bOutImg.length);
        String root = Environment.getExternalStorageDirectory().toString(); // get external storage location
        // get the filename
        int iLastIndexOfDelim = imgURL.lastIndexOf('/');
        String sFileName = imgURL.substring(iLastIndexOfDelim);
        String sFilePath = root + "/" + sFileName; // create path

        // write
        try( FileOutputStream fileOutputStream = new FileOutputStream(sFilePath) ) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
