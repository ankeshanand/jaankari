package jaangari.opensoft.iitkgp.jaankari;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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

public class VideoDownload extends Service {
    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec
    private final String USER_AGENT = "Mozilla/5.0";
    private static ArrayList<String> list;

//    private ArrayList<String> getFileList(){
//        ArrayList<String> files = new ArrayList<String>();
//
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost("http://"+getString(R.string.ip_address)+"videos.php");
////                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
////                nameValuePair.add(new BasicNameValuePair("email", mEmail));
////                nameValuePair.add(new BasicNameValuePair("passwd", mPassword));
////                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//            HttpResponse response = httpClient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            InputStream inputStream = entity.getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            Log.v("Downloaded content","file names : "+line);
////                if ("true\n".equals(sb.toString())) {
////                    return true;
////                } else {
////                    return false;
////                }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return files;
//
//    }


    private boolean download(String path){
        File temp_file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/"+ path);
        if(temp_file.exists()){
            return true;
        }
        String str = "http://"+getString(R.string.ip_address)+path;
        try {

//            URI uri = new URI("http",getString(R.string.ip_address),"VideosActivity",path.substring(path.lastIndexOf("/")+1),null);
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
            File file = new File(Environment.getExternalStorageDirectory()+"/"+getString(R.string.app_name)+"/"+path.substring(0,path.lastIndexOf("/")));
            if(!file.exists()){
                file.mkdirs();
            }
            System.out.println(Environment.getExternalStorageDirectory()+"/"+getString(R.string.app_name)+"/"+path.substring(0,path.lastIndexOf("/")));
            file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/"+ path);
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

            Bitmap bm = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/"+ path,MediaStore.Images.Thumbnails.MINI_KIND);
            file = new File(Environment.getExternalStorageDirectory()+"/" +getString(R.string.app_name)+"/"+ path.substring(0,path.lastIndexOf(".mp4"))+"_thumbnail");
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
                        Log.v(PRINT_SERVICE,"rahul");
                        list = new ArrayList<String>();
                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            for (int i=0;i<len;i++){
                                list.add(jsonArray.get(i).toString());
                            }
                        }
                        for (int i=0;i<list.size();i++){
                            System.out.println(list.get(i));
                            download(list.get(i));
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
