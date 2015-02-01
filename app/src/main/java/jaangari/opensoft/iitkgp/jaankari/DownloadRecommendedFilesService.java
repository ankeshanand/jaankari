package jaangari.opensoft.iitkgp.jaankari;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.Commodity;
import jaangari.opensoft.iitkgp.jaankari.util.Download;
import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;
import jaangari.opensoft.iitkgp.jaankari.util.Weather;

public class DownloadRecommendedFilesService extends Service {
    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec
    private final String USER_AGENT = "Mozilla/5.0";
    private String TAG = "DownloadRecommendationFilesService";
    DatabaseHandler db;
    public DownloadRecommendedFilesService() {
    }

    private boolean Videodownload(String path,int id){
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

            //clean u
            outStream.flush();
            outStream.close();
            inStream.close();
            db = new DatabaseHandler(this.getApplicationContext());
            db.updateVideo(id);
            db.deleteDownload(id,"Video");
            db.closeDB();
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Videos/"
                    + path.substring(path.lastIndexOf("/") + 1), MediaStore.Images.Thumbnails.MINI_KIND);
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

    private boolean dowloadText(String category,int id){
        String url = getString(R.string.ip_address);
        if("News".contains(category)){
            url+="news_download.php";
        }
        else if("Health".contains(category)){
            url+="health_download.php";
        }
        else if("Weather".contains(category)){
            url+="weather_download.php";
        }
        else if("Commodity".contains(category)){
            url+="commodity_download.php";
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("id",""+id));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String json_string = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(json_string);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject root = jsonArray.getJSONObject(i);
                db = new DatabaseHandler(getApplicationContext());

                if("News".contains(category)){
                    News news = new News();
                    news.setPlace(root.getString("place"));
                    news.setText(root.getString("text"));
                    news.setSummary(root.getString("summary"));
                    news.setCategory(root.getInt("category"));
                    news.setTitle("title");
                    news.setID(id);
                    db.addNews(news);
                }
                else if("Health".contains(category)){
                    Health health = new Health();
                    health.setID(id);
                    health.setTitle(root.getString("page_title"));
                    health.setText(root.getString("text"));
                    db.addHealth(health);
                }
                else if("Weather".contains(category)){
                    Weather weather = new Weather();
                    weather.setID(id);
                    weather.setCity(root.getString("city_name"));
                    weather.setDescription(root.getString("description"));
                    weather.setMain(root.getString("main"));
                    weather.setTemp((float)root.getDouble("temp"));
                    weather.setMInTemp((float)root.getDouble("temp_min"));
                    weather.setMaxTemp((float)root.getDouble("temp_max"));
                    weather.setHumidity(root.getInt("humidity"));
                    db.addWeather(weather);
                }
                else if("Commodity".contains(category)){
                    Commodity commodity = new Commodity();
                    commodity.setID(root.getString("id"));
                    commodity.setName(root.getString("name"));
                    commodity.setMax(root.getString("max"));
                    commodity.setMin(root.getString("min"));
                    db.addCommodity(commodity);
                }
                db.deleteDownload(id,category);
                db.closeDB();
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        DownloadFiles mDownlaodFiles = new DownloadFiles();
        mDownlaodFiles.execute((Void)null);
        try {
            while (!mDownlaodFiles.get()) {
                mDownlaodFiles.execute((Void)null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class DownloadFiles extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            db = new DatabaseHandler(getApplicationContext());
            ArrayList<Download> list= db.getToDoDownload();
            db.closeDB();
            for (int i=0;i<list.size();i++){
                Download temp = list.get(i);
                if("Video".contains(temp.category)){
                    db = new DatabaseHandler(getApplicationContext());
                    Videos video = db.getVideobyId(temp.id);
                    Log.d(TAG,video.getName()+ " : " + video.getPath());
                    db.closeDB();
                    if(video.getIsPresent()==0)
                        Videodownload(video.getPath(),temp.id);
                }
                else {
                    dowloadText(temp.category,temp.id);
                }
            }
            return true;
        }
    }
}
