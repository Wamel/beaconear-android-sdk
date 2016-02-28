package com.wamel.beaconear.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;

/**
 * Created by mreverter on 28/2/16.
 */
public class FileUtils {

    public static boolean saveStringInFile(Context context, String filename, String data) {
        boolean succeeded = true;
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            succeeded = false;
        }
        return succeeded;
    }

    public static String getStringFromFile(Context mContext, String filename) {
        File file = new File(mContext.getFilesDir(), filename);
        FileInputStream fin;
        String data = "";
        try {
            fin = new FileInputStream(file);
            data = convertStreamToString(fin);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
