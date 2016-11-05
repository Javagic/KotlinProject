package com.ilya.portfolioproject;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ilya on 11/4/2016.
 */
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            int count;
            while ((count=is.read(bytes, 0, buffer_size))!=-1){
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){ex.printStackTrace();}
    }
}