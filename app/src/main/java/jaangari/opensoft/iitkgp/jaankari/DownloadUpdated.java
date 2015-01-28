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

public class DownloadUpdated extends Service {
    private static ArrayList<Videos> list;
    private DatabaseHandler db;
    private final String TAG = "VideoDownlaodService";




    String email;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        email = sp1.getString("emailId", null);
        db = new DatabaseHandler(this.getApplicationContext());
        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        String url = "http://" + getString(R.string.ip_address) + "toDownload.php";
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                        nameValuePair.add(new BasicNameValuePair("email",email));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                        HttpResponse response = httpclient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
//                        HttpGet httpGet = new HttpGet(url);
//                        HttpResponse response = httpclient.execute(httpGet);
                        String json_string = EntityUtils.toString(response.getEntity());
                        JSONArray jsonArray = new JSONArray(json_string);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject root = jsonArray.getJSONObject(i);
                            db = new DatabaseHandler(getApplicationContext());
                            db.addDownload(root.getInt("id"),root.getString("category"));
                            db.closeDB();
//                            Videos video = new Videos(root.getInt("id"),root.getString("name"),
//                                    root.getString("path"),root.getInt("category"),
//                                    (float)root.getDouble("rating"));
//                            String path = root.getString("path");
//                            File check_file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/Videos/"
//                                    + path.substring(path.lastIndexOf("/")));
//                            if(!check_file.exists()){
//                                db.addVideo(video);
//                                if(Videodownload(video.getPath(),video.getName())){
//                                    Log.v(TAG,"Download Completed");
//                                }
//                                else{
//                                    Log.v(TAG,"Download Failed");
//                                }
//                            }
//                            else{
//                                Log.v(TAG,"In database");
//                            }
//                            Log.v(TAG,root.toString());
//                            db.closeDB();
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
