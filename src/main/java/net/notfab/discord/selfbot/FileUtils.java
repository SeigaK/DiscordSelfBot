package net.notfab.discord.selfbot;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * DiscordSelfBot - http://notfab.net/
 * Created by Fabricio20 on 4/10/2016.
 */
public class FileUtils {

    public static void writeFile(File file, JSONObject content) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try(FileWriter fw = new FileWriter(file.getPath())) {
            fw.write(content.toString(1));
            fw.close();
        } catch (IOException e) {
            System.out.println("Error While Saving File Contents:");
            e.printStackTrace();
        }
    }

    public static JSONObject readFile(File file) {
        if(!file.exists()) {
            return new JSONObject();
        }
        try {
            InputStream is = file.toURI().toURL().openStream();
            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonObject = new JSONObject(tokener);
            is.close();
            return jsonObject;
        } catch (Exception e) {
            System.out.println("Error While Reading JSON File Contents:");
            e.printStackTrace();
        }
        return new JSONObject();
    }

}
