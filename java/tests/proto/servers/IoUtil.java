package servers;

import java.util.Map;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import common.Constants;

public class IoUtil 
{

    public static boolean readFile(String path, UnaryOperator<String> callback)
    {
        try
        {
            File f = new File(Constants.DB_SERVER_INFO_PATH);
            FileReader reader = new FileReader(f);
            BufferedReader buffer = new BufferedReader(reader);
            String line;
            while((line = buffer.readLine()) != null)
            {
                callback.apply(line);
            }
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    public static boolean readFileServerInfo(Map<String, String> dbServerInfos)
    {
        boolean ret = false;
        ret = readFile(Constants.DB_SERVER_INFO_PATH, x -> 
        { 
            readLineServerInfo(x, dbServerInfos);
            return "";
        });
        return ret;
    }

    private static void readLineServerInfo(String line, Map<String, String> dbServerInfos)
    {
        String key = line.split(":")[0];
        String value = line.split(":")[1];

        if(!dbServerInfos.containsKey(key)){ dbServerInfos.put(key, value); }
    }

    public static String getValueStr(Map<String, String> map, String key)
    {
        if(map.containsKey(key))
        {
            return map.get(key);
        }
        return null;
    }

    public static Integer getValueInt(Map<String, String> map, String key)
    {
        if(map.containsKey(key))
        {
            return Integer.valueOf(map.get(key));
        }
        return null;
    }

    public static void Log(String s)
    {
        System.out.println(s);
    }



    
}
