package jaangari.opensoft.iitkgp.jaankari;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.SystemUiHider;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;

public class VideoDownloadService extends Service {
    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec
    private final String USER_AGENT = "Mozilla/5.0";
    private static ArrayList<Videos> list;
    private DatabaseHandler db;
    private final String TAG = "VideoDownlaodService";


    private boolean Videodownload(String path,String filename){
        String str = "http://"+getString(R.string.ip_address)+path;
        try {
            URL url = new URL(str);
            System.out.println(url.toExternalForm());
            //Open a connection to that URL.
            URLConnection ucon = url.openConnection();
            //this timeout affects how long it takes for the app to realize there's a connection problem
            ucon.setReadTimeout(TIMEOUT_CONNECTION);
            ucon.setConnectTimeout(TIMEOUT_SOCKET);
            long startTime = System.currentTimeMillis();
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            File file = new File(Environment.getExternalStorageDirectory()+"/"+getString(R.string.app_name)+"/Videos");
            if(!file.exists()){
                file.mkdirs();
            }
            file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/Videos/"+ path.substring(path.lastIndexOf("/")+1));
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();

            Bitmap bm = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/Videos/"
                    + path.substring(path.lastIndexOf("/")+1),MediaStore.Images.Thumbnails.MINI_KIND);
            file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/Videos/"+
                    path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."))+"_thumbnail.jpg");
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85,outStream);
            outStream.flush();
            outStream.close();
            System.out.println("download completed in "
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");

        } catch (MalformedURLException e) {
            System.out.println("Check the url");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DatabaseHandler(this.getApplicationContext());
        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        String url = "http://" + getString(R.string.ip_address) + "videos.php";
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet(url);
                        HttpResponse response = httpclient.execute(httpGet);
                        String json_string = EntityUtils.toString(response.getEntity());
                        JSONArray jsonArray = new JSONArray(json_string);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject root = jsonArray.getJSONObject(i);
                            Videos video = new Videos(root.getInt("id"),root.getString("name"),
                                    root.getString("path"),root.getInt("category"),
                                    (float)root.getDouble("rating"));
                            String path = root.getString("path");
                            File check_file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/Videos/"
                                    + path.substring(path.lastIndexOf("/")));
                            if(!check_file.exists()){
                                db.addVideo(video);
                                if(Videodownload(video.getPath(),video.getName())){
                                    Log.v(TAG,"Download Completed");
                                }
                                else{
                                    Log.v(TAG,"Download Failed");
                                }
                            }
                            else{
                                Log.v(TAG,"In database");
                            }
                            Log.v(TAG,root.toString());
                            db.closeDB();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            System.out.println("Hello");
        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
