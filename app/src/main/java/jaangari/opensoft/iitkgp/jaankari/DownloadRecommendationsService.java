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

public class DownloadRecommendationsService extends Service {
    private DatabaseHandler db;
    private String email;
    private String TAG = "DownloadRecommendationService";

//    protected void updateToDownload(){
//        try {
//            Thread t = new Thread(new Runnable() {
//                public void run() {
//
//                }
//            });
//            t.start();
//            System.out.println("Hello");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"starting AsyncTask");
        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        email = sp1.getString("emailId", null);
        DownloadRecommendations recommendations = new DownloadRecommendations();
        try {
            recommendations.execute((Void) null);
            while (!recommendations.get()) {
                recommendations.execute((Void)null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent1 = new Intent(this,DownloadRecommendedFilesService.class);
        startService(intent1);
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class DownloadRecommendations extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = "http://" + getString(R.string.ip_address) + "toDownload.php";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("email",email));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String json_string = EntityUtils.toString(entity);
                JSONArray jsonArray = new JSONArray(json_string);
                db = new DatabaseHandler(getApplicationContext());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject root = jsonArray.getJSONObject(i);
                    Log.d(TAG,root.toString());
                    db.addDownload(root.getInt("id"),root.getString("category"));
                }
                db.closeDB();
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

}
