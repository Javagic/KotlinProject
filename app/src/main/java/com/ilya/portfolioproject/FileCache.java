package com.ilya.portfolioproject;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Ilya on 11/4/2016.
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "ParseListViewImgTxt");
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}