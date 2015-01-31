package jaangari.opensoft.iitkgp.jaankari.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import jaangari.opensoft.iitkgp.jaangari.R;

/**
 * Created by rahulanishetty on 1/31/15.
 */
public class AppLog {
    public void appendLog(Context context,String text){
        File logFile = new File(Environment.getExternalStorageDirectory()+"/"
                +context.getString(R.string.app_name)+"/LogFile.txt");
        if(!logFile.exists()){
            try{
                logFile.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.append(text);
            bw.newLine();
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> log(Context context){
        ArrayList<String> list = new ArrayList<String>();
        File logFile = new File(Environment.getExternalStorageDirectory()+"/"
                +context.getString(R.string.app_name)+"/LogFile.txt");
        if(!logFile.exists()){
            return null;
        }
        else{
            try {
                BufferedReader br = new BufferedReader(new FileReader(logFile));
                String str;
                while((str = br.readLine())!=null){
                    list.add(str);
                }
                br.close();
                logFile.delete();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
