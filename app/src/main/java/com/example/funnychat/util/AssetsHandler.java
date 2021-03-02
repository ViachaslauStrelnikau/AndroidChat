package com.example.funnychat.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsHandler {
    private AssetManager assetsManager;

    public AssetsHandler(Context context) {
        this.assetsManager = context.getAssets();
    }

    public String readTextFile(String fileName) {
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            inputStream = assetsManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            System.out.println("Error file reading!" + fileName);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Cant close file:" + fileName);
                }
            } else {
                System.out.println("Error read input stream:" + fileName);
            }
        }

        return stringBuilder.toString();
    }

    public JSONObject parseJSONfile(String fileName) {
        String fileData = this.readTextFile(fileName);
        if (fileData.isEmpty()) {
            return null;
        } else {
            try {
               return new JSONObject(fileData);

            } catch (JSONException e) {
                System.out.println("Cant parse file data");
                return null;
            }
        }
    }

}
