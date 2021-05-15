package com.gymlazy.tripplanner.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloader {

    /**
     * The purpose of this function is used for downloading a image from the given url
     * @param imgURL
     * @throws IOException
     */
    public static void downloadImgFromURL(String imgURL) throws IOException {
        URL url = new URL(imgURL);
        Bitmap bmp = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bOutImg = null;

        // connect to the url and download the image
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        try{
            InputStream in = connection.getInputStream();

            // check whether the response is OK
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        url);
            }

            // loop through the stream of byte and write to the output stream
            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while((byteRead = in.read(buffer)) > 0){
                out.write(buffer, 0, byteRead);
            }
            out.close();
        } finally {
            connection.disconnect();
        }

        // convert byte array to bitmap
        bOutImg = out.toByteArray();
        bmp = BitmapFactory.decodeByteArray(bOutImg, 0 , bOutImg.length);

        String root = Environment.getExternalStorageDirectory().toString(); // get external storage location
        String sFilePath = getImageNameFromURL(imgURL, root);

        // save the image file in external storage
        try( FileOutputStream fileOutputStream = new FileOutputStream(sFilePath) ) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * get the image name from the url
     * @param imgURL
     * @param root
     * @return
     */
    public static String getImageNameFromURL(String imgURL, String root) {
        // get the filename
        int iLastIndexOfDelim = imgURL.lastIndexOf('/');
        String sFileName = imgURL.substring(iLastIndexOfDelim);
        return root + "/" + sFileName;
    }

}
