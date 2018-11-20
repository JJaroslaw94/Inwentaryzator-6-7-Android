package jj.home.myapplication;

import android.content.Context;


import java.io.FileOutputStream;
import java.io.IOException;


public class ZapsywanieWczytywanie {

    public static void ZapisDoPliku(Context context, String fileName, String str) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(str.getBytes(), 0, str.length());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
