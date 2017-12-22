package com.worksit.app.commons.resources;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by SKYNET-DEV01 on 28/09/2017.
 */

public class RawResources {

    public String loadFile(Context ctx, int resource) throws IOException {
        InputStream inputStream = ctx.getResources().openRawResource(resource);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String readLine = null;

        try {
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
