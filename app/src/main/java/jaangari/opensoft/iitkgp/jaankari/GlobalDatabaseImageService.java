package jaangari.opensoft.iitkgp.jaankari;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;
import jaangari.opensoft.iitkgp.jaankari.util.Weather;

public class GlobalDatabaseImageService extends Service {
    private String TAG = "GloabalDatabaseImage";
    private DatabaseHandler db;
    SharedPreferences sp;
    public GlobalDatabaseImageService() {
    }

    public void getNews(){
        try {
            String url = "http://"+getString(R.string.ip_address)+"/getNews.php";
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(json_string);
            db = new DatabaseHandler(getApplicationContext());
            for(int i=0;i<jsonArray.length();i++){
                JSONObject root = jsonArray.getJSONObject(i);
                News news = new News(root.getInt("id"),root.getString("title"),root.getString("summary"),null,null,root.getInt("category"));
                db.addNews(news);
            }
            db.closeDB();
            SharedPreferences sp= getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putBoolean("News",true);
            Ed.commit();
            Log.d(TAG,"News Set");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void getHealth(){
        try{
            String url = "http://"+getString(R.string.ip_address)+"/getHealth.php";
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(json_string);
            db = new DatabaseHandler(getApplicationContext());
            for (int i=0;i<jsonArray.length();i++){
                JSONObject root = jsonArray.getJSONObject(i);
                Health health = new Health(root.getInt("id"),root.getString("page_title"),null);
                db.addHealth(health);

            }
            db.closeDB();
            SharedPreferences sp=getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putBoolean("Health",true);
            Ed.commit();
            Log.d(TAG,"Health Set");
        }catch(Exception e){
//            e.printStackTrace();
        }
    }

    public void getVideos(){
        try{
            String url = "http://"+getString(R.string.ip_address)+"/getVideos.php";
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(json_string);
            db = new DatabaseHandler(getApplicationContext());
            for (int i=0;i<jsonArray.length();i++){
                JSONObject root = jsonArray.getJSONObject(i);
                Videos video = new Videos(root.getInt("id"),root.getString("name"),
                        root.getString("path"),root.getInt("category"),
                        (float)root.getDouble("rating"));
                db.addVideo(video);
                Log.d(TAG,root.toString());
            }
            db.closeDB();
            SharedPreferences sp=getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putBoolean("Videos",true);
            Ed.commit();
            Log.d(TAG,"Videos set");
        }catch(Exception e){
//            e.printStackTrace();
        }
    }

    public void getWeather(){
        try{
            String url = "http://"+getString(R.string.ip_address)+"/getWeather.php";
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(json_string);
            db = new DatabaseHandler(getApplicationContext());
            for (int i=0;i<jsonArray.length();i++){
                JSONObject root = jsonArray.getJSONObject(i);
                Weather weather = new Weather(root.getInt("id"),root.getString("city_name"),
                        root.getString("main"),root.getString("description"),(float)root.getDouble("temp"),
                        (float)root.getDouble("temp_min"),(float)root.getDouble("temp_max"),root.getInt("humidity"));
                    db.addWeather(weather);
            }
            db.closeDB();
            SharedPreferences sp=getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putBoolean("Weather",true);
            Ed.commit();
            Log.d(TAG,"Weather set");
        }catch(Exception e){
            Log.d(TAG,"Adding weather failed");
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        sp = this.getSharedPreferences("Login", 0);
        DownloadImage mDownloadImage = new DownloadImage();
        mDownloadImage.execute((Void)null);
//        Thread t = new Thread(new Runnable(){
//            @Override
//            public void run() {
//            }
//        });
//        t.start();
        Log.d(TAG,"Done");
        return 0;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class DownloadImage extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {


            Log.d(TAG,""+sp.getBoolean("Videos",false));
            if(!sp.getBoolean("Videos",false)){
                Log.e(TAG,"Videos");
                getVideos();
            }
            Log.e(TAG,""+sp.getBoolean("Weather",false));
            if(!sp.getBoolean("Weather",false)){
                Log.e(TAG,"Weather");
                getWeather();
            }
            Log.d(TAG,""+sp.getBoolean("News",false));
            if(!sp.getBoolean("News",false)){
                Log.e(TAG,"News");
                getNews();
            }
            Log.d(TAG,""+sp.getBoolean("Health",false));
            if(!sp.getBoolean("Health",false)){
                Log.e(TAG,"Health");
                getHealth();
            }
            return true;
        }
    }
}
