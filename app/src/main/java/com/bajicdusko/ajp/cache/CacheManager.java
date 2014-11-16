package com.bajicdusko.ajp.cache;

import android.content.Context;

import com.bajicdusko.ajp.json.Model;
import com.bajicdusko.ajp.json.Provider;
import com.bajicdusko.ajp.util.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Bajic on 30-Jul-14.
 */
public class CacheManager<T extends Model> {

    Context context;
    String md5FileName;
    final String FAVORITED_CHANNELS_FILENAME = "favoritedChannels";

    public CacheManager(Context context, String url)
    {
        this.context = context;
        md5FileName = Utilities.md5(url);
    }


    public T GetContent(Class<T> tClass) throws IOException, FileNotFoundException
    {
        String content;
        File file = new File(context.getDir("data", context.MODE_PRIVATE), md5FileName);

        if(!file.exists())
            throw new FileNotFoundException();

        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int length = 0;
        while((length = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, length);
        }

        content = out.toString();
        out.close();
        in.close();
        in.close();

        T t = null;
        try {
           t = Provider.getModelForJson(tClass, context, content);
        }
        catch (Exception ex)
        {
            String dummy = "";
        }

        return t;
    }

    public void WriteContentArray(ArrayList<T> contentArray) throws IOException
    {
        String json = "";
        for(int i = 0; i < contentArray.size(); i++)
        {
            if(i == 0)
                json  += "[" + contentArray.get(i).JSONString;
            else if(i == contentArray.size() - 1)
                json += "," + contentArray.get(i).JSONString + "]";
            else
                json += "," + contentArray.get(i).JSONString;
        }

        WriteContent(json);
    }

    public void WriteContentObject(T contentObject) throws IOException
    {
        String content = contentObject.JSONString;
        WriteContent(content);
    }

    private void WriteContent(String content) throws IOException
    {
        File file = new File(context.getDir("data", context.MODE_PRIVATE), md5FileName);
        if(file.exists())
            file.delete();

        file.createNewFile();

        FileOutputStream out = new FileOutputStream(file);
        byte[] contentBytes = content.getBytes();
        out.write(contentBytes, 0, contentBytes.length);
        out.flush();
        out.close();
    }
}
