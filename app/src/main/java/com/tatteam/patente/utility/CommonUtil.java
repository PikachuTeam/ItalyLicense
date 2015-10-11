package com.tatteam.patente.utility;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ThanhNH on 2/18/2015.
 */
public class CommonUtil {

    public File captureView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap =  view.getDrawingCache();
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.JPEG");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
//            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
//            Log.e("GREC", e.getMessage(), e);
        }
        return imagePath;
    }
}
